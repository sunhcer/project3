package com.stylefeng.guns.rest.promo.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class PromoToken implements Serializable {
    Integer promoId;
    Integer amount;
    String promoToken;
}
