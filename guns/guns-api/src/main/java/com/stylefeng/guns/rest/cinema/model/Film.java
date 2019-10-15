package com.stylefeng.guns.rest.cinema.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class Film implements Serializable {

    private String actors;

    private String filmCats;

    private List<FilmField> filmFields;

    private Integer filmId;

    private String filmLength;

    private String filmName;

    private String filmType;

    private String imgAddress;

}
