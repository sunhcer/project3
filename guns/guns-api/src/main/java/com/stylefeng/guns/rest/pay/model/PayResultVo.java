package com.stylefeng.guns.rest.pay.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class PayResultVo  implements Serializable {
    String orderId;
    String tryNums;
}
