package com.stylefeng.guns.rest.cinema.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class HallInfo implements Serializable {
    private String discountPrice;

    private Integer hallFieldId;

    private String hallName;

    private String price;

    private String seatFile;

    private String soldSeats;
}
