package com.stylefeng.guns.rest.auth;

import lombok.Data;

import java.io.Serializable;

@Data
public class BaseAuthResponseVO implements Serializable {
    Object data;
    String imgPre;
    String msg;
    String nowPage;
    int status;
    String totalPage;

    public BaseAuthResponseVO() {
    }

    public BaseAuthResponseVO(Integer status, Object data) {
        this.status = status;
        this.data = data;
    }
}
