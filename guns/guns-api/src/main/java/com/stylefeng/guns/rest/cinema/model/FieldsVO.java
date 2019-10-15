package com.stylefeng.guns.rest.cinema.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class FieldsVO {

    private CinemaInfo cinemaInfo;

    private List<Film> filmList;

}
