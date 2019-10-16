package com.stylefeng.guns.rest.order.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BaseOrderResponseVO implements Serializable {
    List<OrderInfo> data;
    String imgPre;
    String msg;
    String nowPage;
    int status;
    String totalPage;
}
