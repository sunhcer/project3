package com.stylefeng.guns.rest.modular.promo;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.exception.GunsExceptionEnum;
import com.stylefeng.guns.core.util.ToolUtil;
import com.stylefeng.guns.rest.cinema.model.CinemaInfo;
import com.stylefeng.guns.rest.cinema.service.CinemaService;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoOrderMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimePromoStockMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeStockLogMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromo;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromoOrder;
import com.stylefeng.guns.rest.common.persistence.model.MtimePromoStock;
import com.stylefeng.guns.rest.common.persistence.model.MtimeStockLog;
import com.stylefeng.guns.rest.constant.CacheConstant;
import com.stylefeng.guns.rest.modular.rocketmq.PromoProducer;
import com.stylefeng.guns.rest.promo.model.PromoInfoVO;
import com.stylefeng.guns.rest.promo.model.PromoToken;
import com.stylefeng.guns.rest.promo.model.PromosVO;
import com.stylefeng.guns.rest.promo.service.PromoService;
import com.stylefeng.guns.rest.util.RandomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service(interfaceClass = PromoService.class)
@Component
@Slf4j
public class PromoServiceImpl implements PromoService {
    @Autowired
    MtimePromoMapper mtimePromoMapper;
    @Reference(interfaceClass = CinemaService.class, check = false)
    CinemaService cinemaService;
    @Autowired
    MtimePromoStockMapper mtimePromoStockMapper;
    @Autowired
    MtimePromoOrderMapper mtimePromoOrderMapper;
//    @Autowired
//    Jedis jedis;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    PromoProducer promoProducer;
    @Autowired
    MtimeStockLogMapper stockLogMapper;


    @Override
    public PromosVO selectAllPromo(Integer cinemaId) {

        EntityWrapper<MtimePromo> wrapper = new EntityWrapper<>();
        if (cinemaId != null && cinemaId != 99) {
            //如果有传入cinemaId 则设置这个条件
            wrapper.eq("cinema_id", cinemaId);
        }
        List<MtimePromo> promoList = mtimePromoMapper.selectList(wrapper);

        ArrayList<PromoInfoVO> promoInfoVOArrayList = new ArrayList<>();

        if (promoList != null && promoList.size() > 0) {
            for (MtimePromo mtimePromo : promoList) {
                Integer mtimePromoCinemaId = mtimePromo.getCinemaId();

                CinemaInfo cinemaInfo = cinemaService.getCinemaInfoByCinemaId(mtimePromoCinemaId);

                //因为存的时候加上了前缀
                String key = "stock" + mtimePromo.getUuid().toString();
                log.info("即将获取缓存中的数据,key:{}", key);
                String s = redisTemplate.opsForValue().get(key);
//                String s = jedis.get(key);

                Integer num = 0;
                if (s != null) {
                    num = Integer.parseInt(s);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                PromoInfoVO promoInfoVO = PromoInfoVO.builder()
                        .cinemaAddress(cinemaInfo.getCinemaAdress())
                        .cinemaId(mtimePromo.getCinemaId())
                        .cinemaName(cinemaInfo.getCinemaName())
                        .description(mtimePromo.getDescription())
                        .endTime(sdf.format(mtimePromo.getEndTime()))
                        .imgAddress(cinemaInfo.getImgUrl())
                        .price(mtimePromo.getPrice().intValue())
                        .startTime(sdf.format(mtimePromo.getStartTime()))
                        .status(mtimePromo.getStatus())
                        //从缓存中获取库存
                        .stock(num)
                        .uuid(mtimePromo.getUuid()).build();
                promoInfoVOArrayList.add(promoInfoVO);
            }
        }
        PromosVO promosVO = new PromosVO();
        promosVO.setStatus(0);
        promosVO.setTotalPage(2);
        promosVO.setNowPage(1);
        promosVO.setData(promoInfoVOArrayList);
        return promosVO;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public PromosVO createOrder(PromoToken promoToken, Integer userId) {
        Integer amount = promoToken.getAmount();
        Integer promoId = promoToken.getPromoId();
        PromosVO promosVO = new PromosVO();

        Integer stockNum = null;

        String promoStock = null;

        //检查订单量是否充足
        if (promoId == null) {
            throw new GunsException(GunsExceptionEnum.INVLIDE_PROMO);
        }
        String stockKey = "stock" + promoId.toString();
//        promoStock = jedis.get(stockKey);
        promoStock = redisTemplate.opsForValue().get(stockKey);
        if (promoStock == null) {
            //jedis中没有这个,说明缓存同步失败
            throw new GunsException(GunsExceptionEnum.CACHE_ERROR);
        }

        //代表内存中有    代表是合法的秒杀商品id
        stockNum = Integer.parseInt(promoStock);
        //如果库存大于下单数量    则认为条件满足
        if (stockNum != null && stockNum >= amount) {
            //库存够  扣减库存
//            jedis.incrBy(stockKey, -1*amount);
            redisTemplate.opsForValue().increment(stockKey, -1*amount);
        } else {
            throw new GunsException(GunsExceptionEnum.STOCK_LACK);
        }

//        if (Integer.parseInt(jedis.get(stockKey)) <= 0){
        if (Integer.parseInt(redisTemplate.opsForValue().get(stockKey)) <= 0){
            //库存扣完了 打上一个库存售罄的标志
            String key = CacheConstant.STOCK_EMPTY_FLAG_PREFIX + promoId;
//            jedis.set(key, "stock_empty");
            redisTemplate.opsForValue().set(key, "stock_empty");
        }

        //能走到这里 说明下单成功
        promosVO.setStatus(0);
        promosVO.setMsg("下单成功");
        MtimePromo mtimePromo = mtimePromoMapper.selectById(promoId);

        String nowDateString = RandomUtils.getNowDateString();
        String random = ToolUtil.getRandomString(10);
        String mixRandom = ToolUtil.getRandomString(10);
        //生成订单信息
        MtimePromoOrder order = MtimePromoOrder.builder()
                .uuid(nowDateString + random)
                .userId(userId)
                .cinemaId(mtimePromo.getCinemaId())
                .exchangeCode("EX" + nowDateString + mixRandom)
                .amount(amount)
                .price(mtimePromo.getPrice())
                .startTime(mtimePromo.getStartTime())
                .endTime(mtimePromo.getEndTime())
                .createTime(new Date())
                .build();
        //插入订单
        Integer affectRows = null;
        try {
            affectRows = mtimePromoOrderMapper.insert(order);
        } catch (Exception e) {
            //插入订单失败 将jedis加回来
//            jedis.incrBy(stockKey, amount);
            redisTemplate.opsForValue().increment(stockKey, amount);
            log.error("订单生成失败 promoId:{}    amount:{}", promoId, amount);
            e.printStackTrace();
        }

        if (affectRows < 1){
            //没有插入成功    订单生成失败
            log.error("订单生成失败 promoId:{}    amount:{}", promoId, amount);
//            jedis.incrBy(stockKey, amount);
            redisTemplate.opsForValue().increment(stockKey, amount);
            throw new GunsException(GunsExceptionEnum.ORDER_CREATE_ERROR);
        }
        boolean message = false;

        try {
//            message = promoProducer.sendDecreaseStockMessage(promoId, amount);
//            message = promoProducer.sendDecreaseStockMessage(promoId, amount);
        } catch (Exception e) {
            e.printStackTrace();
//            jedis.incrBy(stockKey, amount);
            redisTemplate.opsForValue().increment(stockKey, amount);
            throw new GunsException(GunsExceptionEnum.MESSAGE_SEND_FAIL);
        }

        if (!message){
//            jedis.incrBy(stockKey, amount);
            redisTemplate.opsForValue().increment(stockKey, amount);
            throw new GunsException(GunsExceptionEnum.MESSAGE_SEND_FAIL);
        }

        return promosVO;
    }


    @Override
    public void storeStock2Redis() {
        List<MtimePromoStock> stocks = mtimePromoStockMapper.selectList(null);
        String numberPrefix = CacheConstant.PROMO_TOKEN_NUMBER_PREFIX;
        Calendar instance = Calendar.getInstance();
        int day = instance.get(Calendar.DATE);

        for (MtimePromoStock stock : stocks) {
            //将其放入库存中
            String stockId = "stock" + stock.getPromoId();
            Integer stockNum = stock.getStock();
            String tokenNum = String.valueOf((stockNum * 5));
            log.info("库存发布到redis中   key:{}  value:{}", stockId, stockNum);
//            jedis.set(stockId, stockNum.toString());
            redisTemplate.opsForValue().set(stockId, stockNum.toString());
            //发放五倍的令牌
            String promoTokenNumber = numberPrefix + stock.getPromoId();
//            jedis.set(promoTokenNumber, tokenNum);
            redisTemplate.opsForValue().set(promoTokenNumber, tokenNum);
        }
        //设置六小时过期
        int secondsOneDay = 60 * 60 * 6;

        //代表今天有发布过(做一个标记)
        String key = CacheConstant.PROMO_PUBLISH_PREFIX + day;

        log.info("storeStock2Redis调用了,即将塞入的是key:{}", key);

//        jedis.set(key, "publish");
//        jedis.expire(key, secondsOneDay);
        redisTemplate.opsForValue().set(key, "publish");
        redisTemplate.expire(key, secondsOneDay, TimeUnit.SECONDS);

    }

    @Override
    public boolean createOrderByTransaction(PromoToken promoToken, int userId) {
        Integer amount = promoToken.getAmount();
        Integer promoId = promoToken.getPromoId();
        PromosVO promosVO = new PromosVO();

        Integer stockNum = null;

        String promoStock = null;

        //检查订单量是否充足
        if (promoId == null) {
            throw new GunsException(GunsExceptionEnum.INVLIDE_PROMO);
        }

        String stockKey = "stock" + promoId.toString();
//        promoStock = jedis.get(stockKey);
        promoStock = redisTemplate.opsForValue().get(stockKey);
        if (promoStock == null) {
            throw new GunsException(GunsExceptionEnum.CACHE_ERROR);
        }

        //代表内存中有    代表是合法的秒杀商品id
        stockNum = Integer.parseInt(promoStock);
        //如果库存大于下单数量    则认为条件满足
        if (stockNum != null && stockNum >= amount) {
            //库存够  扣减库存
//            jedis.incrBy(stockKey, -1*amount);
            redisTemplate.opsForValue().increment(stockKey, -1*amount);
            if (stockNum == amount){
                String key = CacheConstant.STOCK_EMPTY_FLAG_PREFIX + promoId;
                //将其设置为已经售空
                redisTemplate.opsForValue().set(key, "stock_empty");
            }
        } else {
            throw new GunsException(GunsExceptionEnum.STOCK_LACK);
        }

        //能走到这里 说明下单成功
        MtimePromo mtimePromo = mtimePromoMapper.selectById(promoId);

        String nowDateString = RandomUtils.getNowDateString();
        String random =  ToolUtil.getRandomString(10);
        String mixRandom =  ToolUtil.getRandomString(10);
        //生成订单信息
        MtimePromoOrder order = MtimePromoOrder.builder()
                .uuid(nowDateString + random)
                .userId(userId)
                .cinemaId(mtimePromo.getCinemaId())
                .exchangeCode("EX" + nowDateString + mixRandom)
                .amount(amount)
                .price(mtimePromo.getPrice())
                .startTime(mtimePromo.getStartTime())
                .endTime(mtimePromo.getEndTime())
                .createTime(new Date())
                .build();
        //插入订单
        Integer affectRows = null;
        try {
            affectRows = mtimePromoOrderMapper.insert(order);
        } catch (Exception e) {
            //插入订单失败 将jedis加回来
//            jedis.incrBy(stockKey, amount);
            redisTemplate.opsForValue().increment(stockKey, amount);
            log.error("订单生成失败 promoId:{}    amount:{}", promoId, amount);
            e.printStackTrace();
        }

        if (affectRows < 1){
            //没有插入成功    订单生成失败
            log.error("订单生成失败 promoId:{}    amount:{}", promoId, amount);
//            jedis.incrBy(stockKey, amount);
            redisTemplate.opsForValue().increment(stockKey, amount);
            throw new GunsException(GunsExceptionEnum.ORDER_CREATE_ERROR);
        }

        return true;
    }

    @Override
    public boolean packageOrderProducer(PromoToken promoToken, int userId) {
        return promoProducer.sendTransactionStockMessage(promoToken, userId);
    }

    /**
     * 日志流水初始化  1代表初始化状态    2表示成功状态     3表示失败状态
     * @param promoToken    封装参数的bean 包含promoId、amount
     * @return  插入成功返回的是流水表的uuid,失败则是null
     */
    @Override
    public String initPromoLog(PromoToken promoToken) {
        Integer amount = promoToken.getAmount();
        Integer promoId = promoToken.getPromoId();

        String uuid = ToolUtil.getRandomString(15);
        MtimeStockLog mtimeStockLog = MtimeStockLog.builder()
                .uuid(uuid)
                .amount(amount)
                .promoId(promoId)
                .status(1).build();

        int affectRows = stockLogMapper.insertLog(mtimeStockLog);
        if (affectRows < 1){
            return null;
        }
        return uuid;
    }

    @Override
    public String generateToken(Integer promoId, String userId) {
        String key = CacheConstant.STOCK_EMPTY_FLAG_PREFIX + promoId;
        String keyPromo = CacheConstant.PROMO_TOKEN_NUMBER_PREFIX + promoId;

//        String flag_empty_promo = jedis.get(key);
        String flag_empty_promo = redisTemplate.opsForValue().get(key);
        if (flag_empty_promo != null){
            //如果有售罄的标志      直接返回null
            return null;
        }

//        Long aLong = jedis.incrBy(keyPromo, -1);
        Long aLong = redisTemplate.opsForValue().increment(keyPromo, -1);
        if (aLong < 0){
            //代表token发放完毕
            return null;
        }


        String tokenFlag = CacheConstant.PROMO_TOKEN_PREFIX + promoId + "_" + userId;
        String token = ToolUtil.getRandomString(10);

//        jedis.set(tokenFlag, token);
        redisTemplate.opsForValue().set(tokenFlag, token);

        return token;

    }
}
