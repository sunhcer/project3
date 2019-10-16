package com.stylefeng.guns.rest.order.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderInfo implements Serializable {
    String cinemaName;
    String fieldTime;
    String filmName;
    String orderId;
    String orderPrice;
    String orderStatus;
    String orderTimestamp;
    String seatsName;
}
