package com.stylefeng.guns.rest.user.model;

import lombok.Data;

import java.util.List;

@Data
public class BaseUserResponseVO<T> {
    List<T> data;
    String imgPre;
    String msg;
    int nowPage;
    int status;
    int totalPage;
}
