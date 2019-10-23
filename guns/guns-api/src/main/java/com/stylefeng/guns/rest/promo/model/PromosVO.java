package com.stylefeng.guns.rest.promo.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PromosVO<T> implements Serializable {
    T data;
    String imgPre;
    Integer nowPage;
    Integer status;
    Integer totalPage;
    String msg;

    public static PromosVO fail(String message){
        PromosVO<Object> promosVO = new PromosVO<>();
        promosVO.setStatus(9);
        promosVO.setMsg(message);
        return promosVO;
    }

    public static PromosVO success(String message){
        PromosVO<Object> promosVO = new PromosVO<>();
        promosVO.setStatus(0);
        promosVO.setMsg(message);
        return promosVO;
    }
}
