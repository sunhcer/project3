package com.stylefeng.guns.rest.film.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/15
 * @Time 15:01
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchFilmByIdBean implements Serializable {
    private String filmEnName;
    private String filmId;
    private String filmName;
    private String imgAddress;
    private String info01;
    private String info02;
    private String info03;
    private FilmInfo info04;
    private String score;
    private String scoreNum;
    private String totalBox;


}
