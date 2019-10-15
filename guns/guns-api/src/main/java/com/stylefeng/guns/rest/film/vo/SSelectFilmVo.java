package com.stylefeng.guns.rest.film.vo;

import lombok.Data;

import java.io.Serializable;
@Data
public class SSelectFilmVo<T> implements Serializable {
    T data;
    String imgPre;
    String msg;
    int nowPage;
    int status;
    int totalPage;
}
