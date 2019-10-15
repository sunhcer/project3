package com.stylefeng.guns.rest.cinema.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HallInfoVO {

    private CinemaInfo cinemaInfo;

    private Film filmInfo;

    private HallInfo hallInfo;

}
