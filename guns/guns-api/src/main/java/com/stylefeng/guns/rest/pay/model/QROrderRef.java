package com.stylefeng.guns.rest.pay.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class QROrderRef implements Serializable {
    double orderPrice;
    String UUID;
    String cinemaName;
    String filmName;
    String seatsIds;
}
