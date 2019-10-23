package com.stylefeng.guns.rest.modular.promo;


import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.util.concurrent.RateLimiter;
import com.stylefeng.guns.core.exception.GunsException;
import com.stylefeng.guns.core.exception.GunsExceptionEnum;
import com.stylefeng.guns.rest.constant.CacheConstant;
import com.stylefeng.guns.rest.modular.auth.util.JedisUtil;
import com.stylefeng.guns.rest.promo.model.PromoToken;
import com.stylefeng.guns.rest.promo.model.PromosVO;
import com.stylefeng.guns.rest.promo.service.PromoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@Slf4j
public class PromoController {
    @Reference(interfaceClass = PromoService.class, check = false)
    PromoService promoService;
    //    @Autowired
//    Jedis jedis;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    JedisUtil jedisUtil;

    private ExecutorService executorService;
    private RateLimiter rateLimiter;
    private RateLimiter rateLimiterCreateOrder;

    @PostConstruct
    public void init() {
        executorService = Executors.newFixedThreadPool(50);
        //生成token的桶大小
        rateLimiter = RateLimiter.create(500);
        rateLimiterCreateOrder = RateLimiter.create(100);
    }

    ///promo/publishPromoStock
    @RequestMapping("/promo/publishPromoStock")
    public PromosVO publishPromoStock() {

        Calendar instance = Calendar.getInstance();
        int day = instance.get(Calendar.DATE);
//        String publish = jedis.get(CacheConstant.PROMO_PUBLISH_PREFIX + day);
        String publish = redisTemplate.opsForValue().get(CacheConstant.PROMO_PUBLISH_PREFIX + day);
        log.info("发布消息窗口,状态是:{}", publish);
        if (publish == null) {
            //从数据库中取出库存放到redis
            log.info("调用了发布窗口");
            promoService.storeStock2Redis();
        }

        PromosVO<Object> promosVO = PromosVO.builder()
                .msg("发布成功")
                .status(0).build();
        return promosVO;
    }

    ///promo/getPromo
    @RequestMapping("/promo/getPromo")
    public PromosVO getPromoInfo(Integer cinemaId) {
        PromosVO promosVO = promoService.selectAllPromo(cinemaId);

        return promosVO;
    }

    //promo/generateToken
    @RequestMapping("/promo/generateToken")
    public PromosVO generateToken(Integer promoId, HttpServletRequest request) {

        double acquire = rateLimiter.acquire();
        if (acquire < 0) {
            //获取令牌失败        代表没有睡眠
            return PromosVO.fail("下单失败");
        }

        String userId = jedisUtil.getUserId(request);
        String token = promoService.generateToken(promoId, userId);
        PromosVO<Object> tokenVO = new PromosVO<>();
        if (token == null) {
            //生成token失败
            tokenVO.setStatus(1);
            tokenVO.setMsg("获取失败");
        } else {
            tokenVO.setStatus(0);
            tokenVO.setMsg(token);
        }

        return tokenVO;
    }

    ///promo/createOrder
//    @RequestMapping("/promo/createOrder")
//    public PromosVO createOrder(PromoToken promoToken, HttpServletRequest request){
//        //首先校验token是否存在
//        String userIdString = jedisUtil.getUserId(request);
//        int userId = Integer.parseInt(userIdString);
//        PromosVO promosVO = null;
//        try {
//            promosVO = promoService.createOrder(promoToken, userId);
//        } catch (Exception e) {
//            e.printStackTrace();
//            GunsException g = null;
//            if (e instanceof GunsException){
//                g =  (GunsException) e;
//            }else{
//                g = new GunsException(GunsExceptionEnum.SERVER_ERROR);
//            }
//            promosVO.setStatus(g.getCode());
//            promosVO.setMsg(g.getMessage());
//        }
//        return promosVO;
//    }

    @RequestMapping("/promo/createOrder")
    public PromosVO createOrderByTransaction(PromoToken promoToken, HttpServletRequest request) {
        PromosVO promosVO = new PromosVO();
        //首先校验token是否存在

        double acquire = rateLimiterCreateOrder.acquire();
        if (acquire < 0) {
            return PromosVO.fail("下单失败");
        }

        Integer promoId = promoToken.getPromoId();
        String userIdString = jedisUtil.getUserId(request);
        int userId = Integer.parseInt(userIdString);
        String tokenFromFront = promoToken.getPromoToken();     //从前端传过来的token

        if (promoId == null || tokenFromFront == null) {
            return PromosVO.fail("下单失败");
        }

        String keyToken = CacheConstant.PROMO_TOKEN_PREFIX + promoId.toString() + "_" + userIdString;
//        String tokenFromRedis = jedis.get(keyToken);
        String tokenFromRedis = redisTemplate.opsForValue().get(keyToken);
        if (tokenFromRedis == null) {
            //token不存在  说明token不对
            return PromosVO.fail("下单失败");
        }

        if (!tokenFromRedis.equals(tokenFromFront)) {
            return PromosVO.fail("下单失败");
        }

        String key = CacheConstant.STOCK_EMPTY_FLAG_PREFIX + promoId;

        String flag_empty_promo = redisTemplate.opsForValue().get(key);
        if (flag_empty_promo != null){
            //如果有售罄的标志      直接返回下单失败
            return PromosVO.fail("下单失败");
        }


        Future future = executorService.submit(() -> {
            boolean flag = false;
            try {
                flag = promoService.packageOrderProducer(promoToken, userId);
            } catch (Exception e) {
                throw new GunsException(GunsExceptionEnum.ORDER_CREATE_ERROR);
            }
            if (!flag) {
                throw new GunsException(GunsExceptionEnum.ORDER_CREATE_ERROR);
            }
        });

        try {
            Object o = future.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return PromosVO.fail("下单失败");
        } catch (ExecutionException e) {
            e.printStackTrace();
            return PromosVO.fail("下单失败");
        } catch (GunsException e) {
            e.printStackTrace();
            return PromosVO.fail("下单失败");
        }
        promosVO.setStatus(0);
        promosVO.setMsg("下单成功");

        return promosVO;
    }
}
