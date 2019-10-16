package com.stylefeng.guns.rest.order.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class MyOrderPage  implements Serializable {
    int nowPage;
    int pageSize;
}
