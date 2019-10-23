package com.stylefeng.guns.rest.film.model;

import lombok.Data;

import java.io.Serializable;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/14
 * @Time 19:58
 */
@Data
public class BaseFilmResponseVO<T> implements Serializable {
    private String imgPre;
    private String msg;
    private String nowPage;
    private Integer status;
    private String totalPage;
    private Integer code;
    private T data;

    public static BaseFilmResponseVO fail(String message){
        BaseFilmResponseVO<Object> responseVO = new BaseFilmResponseVO<>();

        responseVO.setCode(999);
        responseVO.setMsg(message);
        return responseVO;
    }
}
