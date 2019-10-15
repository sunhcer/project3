package com.stylefeng.guns.rest.film.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class SSelctFilmReceiveVo implements Serializable {
    int showType;
    int sortId;
    int catId;
    int sourceId;
    int yearId;
    int pageSize;
    int offset;
}
