package com.stylefeng.guns.rest.cinema.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BaseResultVO {

    private Object data;

    private String imgPre;

    private String msg;

    private Integer nowPage;

    private Integer status;

    private Integer totalPage;
}
