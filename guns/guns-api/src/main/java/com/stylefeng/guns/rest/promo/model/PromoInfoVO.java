package com.stylefeng.guns.rest.promo.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PromoInfoVO implements Serializable {
    private String cinemaAddress;
    private Integer cinemaId;
    private String cinemaName;
    private String description;
    private Object endTime;
    private String imgAddress;
    private Integer price;
    private Object startTime;
    private Integer status;
    private Integer stock;
    private Integer uuid;
}
