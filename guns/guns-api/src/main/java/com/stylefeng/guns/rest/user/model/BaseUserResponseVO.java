package com.stylefeng.guns.rest.user.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BaseUserResponseVO<T> implements Serializable {
    List<T> data;
    String imgPre;
    String msg;
    int nowPage;
    int status;
    int totalPage;
}
