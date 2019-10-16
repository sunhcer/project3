package com.stylefeng.guns.rest.modular.order;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stylefeng.guns.rest.cinema.model.CinemaInfo;
import com.stylefeng.guns.rest.cinema.service.CinemaService;
import com.stylefeng.guns.rest.common.persistence.dao.MoocOrderTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import com.stylefeng.guns.rest.film.model.FilmDetail;
import com.stylefeng.guns.rest.film.service.FilmService;
import com.stylefeng.guns.rest.order.model.BuyTicketsVO;
import com.stylefeng.guns.rest.order.model.OrderDetailVO;
import com.stylefeng.guns.rest.order.model.Seat;
import com.stylefeng.guns.rest.order.model.SeatsInfo;
import com.stylefeng.guns.rest.order.service.OrderService;
import com.stylefeng.guns.rest.user.model.BaseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service(interfaceClass = OrderService.class)
@Component
public class OrderServiceImpl implements OrderService {

    @Autowired
    Jedis jedis;
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

        //查询场次信息
        Integer fieldId = buyTicketsVO.getFieldId();
        MtimeFieldT mtimeFieldT = mtimeFieldTMapper.selectById(fieldId);
        if (mtimeFieldT == null){
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

        if (filmDetail == null || cinemaInfo == null){
            baseVo.setStatus(1);
            baseVo.setMsg("查询电影或者影院信息错误");
            return baseVo;
        }

        //信息无误 开始处理业务逻辑
        String soldSeats = buyTicketsVO.getSoldSeats();
        //下订单的座位List
        List<String> soldSeatList = Arrays.asList(soldSeats.split(","));
        if (soldSeatList == null || soldSeatList.size() == 0){
            baseVo.setStatus(1);
            baseVo.setMsg("座位信息不能为空");
            return baseVo;
        }

        String json = jedis.get("12314.json");

        ObjectMapper objectMapper = new ObjectMapper();
        SeatsInfo seatsInfo = null;
        try {
            seatsInfo = objectMapper.readValue(json, SeatsInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //判断有没有这个座位
        //所有座位信息
        List<String> stringList = Arrays.asList(seatsInfo.getIds().toString().split(","));
        boolean flag = oneListContainsAnother(stringList, soldSeatList);
        if (flag == false){
            //座位信息有误 座位不存在
            baseVo.setStatus(1);
            baseVo.setMsg("座位不存在");
            return baseVo;
        }

        String seatsName = buyTicketsVO.getSeatsName();


        //查询数据库 查看是否已经出售过
        EntityWrapper<MoocOrderT> wrapper = new EntityWrapper<>();
        wrapper.eq("field_id", fieldId);
        wrapper.in("order_status",new Integer[]{0,1});
        List<MoocOrderT> moocOrderTList = moocOrderTMapper.selectList(wrapper);

        for (MoocOrderT moocOrderT : moocOrderTList) {
            //这是订单中已经出售的座位id
            String seatsIds = moocOrderT.getSeatsIds();
            List<String> seatList = Arrays.asList(seatsIds.split(","));
            //准备买的几个座位
            if (!oneListHasAnother(seatList, soldSeatList)){
                //如果校验不通过  代表售票失败
                baseVo.setStatus(1);
                baseVo.setMsg("座位已被售出");
                return baseVo;
            }
        }

        //合并所有座位  single是所有
        List<List<Seat>> single = seatsInfo.getSingle();
        List<List<Seat>> couple = seatsInfo.getCouple();
        single.addAll(couple);

        //订单id
        String outTradeNo = String.valueOf((System.currentTimeMillis() + (long) (Math.random() * 10000000L)));
        String filmName = filmDetail.getFilmName();
        Date orderTime = new Date();
        String orderTimeStamp = String.valueOf(System.currentTimeMillis());
        StringBuilder sb = new StringBuilder();
        for (List<Seat> list : single) {
            for (Seat seat : list) {
                if (soldSeatList.contains(seat.getSeatId())){
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
     * @param one
     * @param another
     * @return
     */
    private boolean oneListContainsAnother(List<String> one, List<String> another){
        for (String s : another) {
            if (!one.contains(s)){
                return false;
            }
        }
        return true;
    }

    /**
     * 查看一个list是否包含一个
     * @param one
     * @param another
     * @return
     */
    private boolean oneListHasAnother(List<String> one, List<String> another){
        //one是出售过的  只要出售的包含another 就代表售出失败
        for (String buy : another) {
            if (one.contains(buy)){
                //如果包含的话
                return false;
            }
        }
        //审核通过 代表没有卖出去过
        return true;
    }
}
