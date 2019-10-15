package com.stylefeng.guns.rest.user.model;

import lombok.Data;

import java.io.Serializable;

/**
 * 类简介：
 * 当前方法：
 * 创建时间: 2019-10-15 9:05
 *
 * @author EGGE
 */
@Data
public class BaseVo implements Serializable {
   private Integer status;
   private Object data;
   private String msg;

    public static BaseVo successVo(Object data , String msg){
        BaseVo baseVo=new BaseVo();
        baseVo.setStatus(0);
        baseVo.setData(data);
        baseVo.setMsg(msg);
        return baseVo;
    }
    public static BaseVo errorVo(Integer status , String msg){
        BaseVo baseVo=new BaseVo();
        baseVo.setStatus(status);
        baseVo.setMsg(msg);
        return baseVo;
    }
}
