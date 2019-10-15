package com.stylefeng.guns.rest.user.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 类简介：
 * 当前方法：
 * 创建时间: 2019-10-14 21:40
 *
 * @author EGGE
 */
@Data
public class MtimeUserInfo implements Serializable {
   private Integer uuid;
   private String username;
   private String nickname;
   private String email;
   private String address;
   private String phone;
   private Integer sex;
   private String birthday;
   private Integer lifeState;
   private String biography;
   private String headAdress;
   private Date createTime;
   private Date updateTime;
}
