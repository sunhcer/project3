package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylefeng.guns.rest.cinema.model.CinemaInfo;
import com.stylefeng.guns.rest.cinema.model.Film;
import com.stylefeng.guns.rest.cinema.model.HallInfo;
import com.stylefeng.guns.rest.cinema.service.CinemaService;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import com.stylefeng.guns.rest.film.model.FilmDetail;
import com.stylefeng.guns.rest.film.service.FilmService;
import com.stylefeng.guns.rest.order.model.*;
import com.stylefeng.guns.rest.order.service.OrderService;
import com.stylefeng.guns.rest.user.model.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service(interfaceClass = OrderService.class)
@Component
public class OrderServiceImpl implements OrderService {

    //    @Autowired
//    Jedis jedis;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Reference(interfaceClass = CinemaService.class, check = false)
    CinemaService cinemaService;
    @Reference(interfaceClass = FilmService.class, check = false)
    FilmService filmService;
    @Autowired
    MtimeFieldTMapper mtimeFieldTMapper;
    @Autowired
    MoocOrderTMapper moocOrderTMapper;

    @Override
    public BaseVo buyTickets(String userId, BuyTicketsVO buyTicketsVO) {
        //首先判断信息有没有错误
        BaseVo baseVo = new BaseVo();

        updateOrderInfo();

        //查询场次信息
        Integer fieldId = buyTicketsVO.getFieldId();
        MtimeFieldT mtimeFieldT = mtimeFieldTMapper.selectById(fieldId);
        if (mtimeFieldT == null) {
            //查询到的场次为null  代表本次查询失败  直接返回
            baseVo.setStatus(1);
            baseVo.setMsg("查询场次失败 请联系管理员处理");
            return baseVo;
        }

        //查询其他信息是否正确
        //影院信息
        CinemaInfo cinemaInfo = cinemaService.getCinemaInfoByCinemaId(mtimeFieldT.getCinemaId());
        //电影信息
        FilmDetail filmDetail = filmService.selectFilmById(mtimeFieldT.getFilmId());

        if (filmDetail == null || cinemaInfo == null) {
            baseVo.setStatus(1);
            baseVo.setMsg("查询电影或者影院信息错误");
            return baseVo;
        }

        //信息无误 开始处理业务逻辑
        String soldSeats = buyTicketsVO.getSoldSeats();
        //下订单的座位List
        List<String> soldSeatList = Arrays.asList(soldSeats.split(","));
        if (soldSeatList == null || soldSeatList.size() == 0) {
            baseVo.setStatus(1);
            baseVo.setMsg("座位信息不能为空");
            return baseVo;
        }

        HallInfo hallInfo = cinemaService.getHallInfoByFieldId(mtimeFieldT.getUuid());
//        String json = jedis.get(hallInfo.getSeatFile());
        String json = redisTemplate.opsForValue().get(hallInfo.getSeatFile());

        ObjectMapper objectMapper = new ObjectMapper();
        SeatsInfo seatsInfo = null;
        try {
            seatsInfo = objectMapper.readValue(json, SeatsInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
            baseVo.setStatus(1);
            baseVo.setMsg("座位解析失败 请联系管理员");
            return baseVo;
        }

        //判断有没有这个座位
        //所有座位信息
        List<String> stringList = Arrays.asList(seatsInfo.getIds().toString().split(","));
        boolean flag = oneListContainsAnother(stringList, soldSeatList);
        if (flag == false) {
            //座位信息有误 座位不存在
            baseVo.setStatus(1);
            baseVo.setMsg("座位不存在");
            return baseVo;
        }

        String seatsName = buyTicketsVO.getSeatsName();


        //查询数据库 查看是否已经出售过
        EntityWrapper<MoocOrderT> wrapper = new EntityWrapper<>();
        wrapper.eq("field_id", fieldId);
        wrapper.in("order_status", new Integer[]{0, 1});
        List<MoocOrderT> moocOrderTList = moocOrderTMapper.selectList(wrapper);

        for (MoocOrderT moocOrderT : moocOrderTList) {
            //这是订单中已经出售的座位id
            String seatsIds = moocOrderT.getSeatsIds();
            List<String> seatList = Arrays.asList(seatsIds.split(","));
            //准备买的几个座位
            if (!oneListHasAnother(seatList, soldSeatList)) {
                //如果校验不通过  代表售票失败
                baseVo.setStatus(1);
                baseVo.setMsg("座位已被售出");
                return baseVo;
            }
        }

        //合并所有座位  single是所有
        List<List<Seat>> single = seatsInfo.getSingle();
        List<List<Seat>> couple = seatsInfo.getCouple();
        if (couple != null) {
            single.addAll(couple);
        }

        //订单id
        String outTradeNo = String.valueOf((System.currentTimeMillis() + (long) (Math.random() * 10000000L)));
        String filmName = filmDetail.getFilmName();
        Date orderTime = new Date();
        String orderTimeStamp = String.valueOf(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (List<Seat> list : single) {
            for (Seat seat : list) {
                if (soldSeatList.contains(seat.getSeatId())) {
                    //如果是这个座位
                    sb.append(seat.getRow() + "排" + seat.getColumn() + "列 ");
                }
            }
        }
        String seatName = sb.toString();
        Integer orderPrice = mtimeFieldT.getPrice() * soldSeatList.size();
        MoocOrderT moocOrderT = MoocOrderT.builder()
                .uuid(outTradeNo)
                .cinemaId(cinemaInfo.getCinemaId())
                .fieldId(mtimeFieldT.getUuid())
                .filmId(filmDetail.getFilmId())
                .seatsIds(soldSeats)
                .seatsName(seatName)
                .filmPrice(mtimeFieldT.getPrice().doubleValue())
                .orderPrice(orderPrice.doubleValue())
                .orderStatus(0)
                .orderTime(orderTime)
                .orderUser(Integer.valueOf(userId)).build();

        moocOrderTMapper.insert(moocOrderT);

        String format = new SimpleDateFormat("yyyy-MM-dd").format(orderTime);

        String beginTime = mtimeFieldT.getBeginTime();

        OrderDetailVO detailVO = OrderDetailVO.builder()
                .orderId(outTradeNo)
                .filmName(filmDetail.getFilmName())
                .fieldTime(format + " " + beginTime)
                .cinemaName(cinemaInfo.getCinemaName())
                .seatsName(seatName)
                .orderPrice(orderPrice.toString())
                .orderTimestamp(orderTimeStamp).build();

        baseVo.setStatus(0);
        baseVo.setData(detailVO);

        return baseVo;
    }


    /**
     * 查看一个list是否包含另一个list
     *
     * @param one
     * @param another
     * @return
     */
    private boolean oneListContainsAnother(List<String> one, List<String> another) {
        for (String s : another) {
            if (!one.contains(s)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 查看一个list是否包含一个
     *
     * @param one
     * @param another
     * @return
     */
    private boolean oneListHasAnother(List<String> one, List<String> another) {
        //one是出售过的  只要出售的包含another 就代表售出失败
        for (String buy : another) {
            if (one.contains(buy)) {
                //如果包含的话
                return false;
            }
        }
        //审核通过 代表没有卖出去过
        return true;
    }


    @Override
    public List<OrderInfo> getMyOrderInfo(MyOrderPage myOrderPage, String userId) {
        updateOrderInfo();

        Page<MoocOrderT> page = new Page<>();
        page.setSize(myOrderPage.getPageSize());
        page.setCurrent(myOrderPage.getNowPage());

        EntityWrapper<MoocOrderT> entityWrapper = new EntityWrapper<>();
        entityWrapper.eq("order_user", userId);
        entityWrapper.orderBy("order_time",false);
        List<MoocOrderT> list = moocOrderTMapper.selectPage(page, entityWrapper);
        List<OrderInfo> list1 = new ArrayList<>();
        for (MoocOrderT order : list) {  //拿到每个小定单
            OrderInfo orderInfo = new OrderInfo();
            String cinemaName = cinemaService.getCinemaInfoByCinemaId(order.getCinemaId()).getCinemaName();
            orderInfo.setCinemaName(cinemaName);//  1
            MtimeFieldT field = mtimeFieldTMapper.selectById(order.getFieldId());
            orderInfo.setFieldTime("19年10月16日" + field.getBeginTime());//  2
            Film film = cinemaService.getFilmInfoByFieldId(order.getFieldId());
            orderInfo.setFilmName(film.getFilmName());//    3
            orderInfo.setOrderId(order.getUuid());//     4
            orderInfo.setOrderPrice(order.getOrderPrice().toString());//     5
            //0-待支付,1-已支付,2-已关闭
            Integer status = order.getOrderStatus();
            if (status == 0) {
                orderInfo.setOrderStatus("待支付");
            } else if (status == 1) {
                orderInfo.setOrderStatus("已支付");
            } else {
                orderInfo.setOrderStatus("已关闭");//     6
            }
//            orderInfo.setOrderTimestamp("1571206839");//     7
            orderInfo.setOrderTimestamp(String.valueOf(order.getOrderTime().getTime() / 1000));//     7
            orderInfo.setSeatsName(order.getSeatsIds());//  8
            list1.add(orderInfo);
        }
        return list1;
    }

    /**
     * 调用查询之前 将订单的状态更新一下
     */
    public void updateOrderInfo() {
        EntityWrapper<MoocOrderT> wrapper = new EntityWrapper<>();
        wrapper.eq("order_status", 0);      //如果是未付款状态  则在下单15分钟后取消该订单
        wrapper.le("order_time", new Date(System.currentTimeMillis() - 1000 * 60 * 15));

        MoocOrderT orderT = new MoocOrderT();
        orderT.setOrderStatus(2);
        moocOrderTMapper.update(orderT, wrapper);
    }
}



