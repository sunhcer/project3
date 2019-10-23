package com.stylefeng.guns.rest.film.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/14
 * @Time 20:26
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilmDetail implements Serializable {
    Integer boxNum;
    Integer expectNum;
    String filmCats;
    Integer filmId;
    String filmLength;
    String filmName;
    String filmScore;
    Integer filmType;
    String imgAddress;
    String score;
    Object showTime;

}
