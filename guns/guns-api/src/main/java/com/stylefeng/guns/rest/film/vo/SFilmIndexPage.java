package com.stylefeng.guns.rest.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SFilmIndexPage<T> implements Serializable {
    T data;
    String imgPre;
    String msg;
    String nowPage;
    int status;
    String totalPage;
}
