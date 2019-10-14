package com.stylefeng.guns.rest.film.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/14
 * @Time 20:00
 */
@Data
public class FilmsDetail implements Serializable {
    Integer filmNum;
    Integer nowPage;
    Integer totalPage;
    List<FilmDetail> filmInfo;
}
