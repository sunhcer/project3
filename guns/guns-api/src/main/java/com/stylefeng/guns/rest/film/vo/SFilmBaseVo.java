package com.stylefeng.guns.rest.film.vo;

import lombok.Data;

@Data
public class SFilmBaseVo<T> {
    T data;
    String imgPre;
    String msg;
    String nowPage;
    int status;
    String totalPage;
}
