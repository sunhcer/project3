package com.stylefeng.guns.rest.cinema.model;

import java.io.Serializable;
import java.util.Date;

public class BaseRespVo<T> implements Serializable {
    private static final long serialVersionUID = -8097358582185724680L;
    T data;
    String imgPre;
    String msg;
    Integer nowPage;
    Integer totalPage;
    Integer status;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getImgPre() {
        return imgPre;
    }

    public void setImgPre(String imgPre) {
        this.imgPre = imgPre;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Integer getNowPage() {
        return nowPage;
    }

    public void setNowPage(Integer nowPage) {
        this.nowPage = nowPage;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public static BaseRespVo success(Object data){
        BaseRespVo<Object> baseRespVo = new BaseRespVo<>();

        baseRespVo.setData(data);
        baseRespVo.setStatus(0);
        baseRespVo.setMsg(null);
        return baseRespVo;
    }
}
