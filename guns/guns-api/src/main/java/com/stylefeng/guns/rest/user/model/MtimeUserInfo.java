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
    private String uuid;
    private String username;
    private String nickname;
    private String email;
    private String address;
    private String phone;
    private String sex;
    private String birthday;
    private String lifeState;
    private String biography;
    private String headAdress;
    private String createTime;
    private String updateTime;

//    public static MtimeUserInfo transferUser(MtimeUserT userT) {
//        MtimeUserInfo userInfo = new MtimeUserInfo();
//        userInfo.setAddress(userT.getAddress());
//        userInfo.setBiography(userT.getBiography());
//        userInfo.setBirthday(userT.getBirthday());
//        userInfo.setEmail(userT.getEmail());
//        userInfo.setCreateTime(userT.getBeginTime());
//        userInfo.setHeadAdress(userT.getHeadUrl());
//        userInfo.setLifeState(userT.getLifeState());
//        userInfo.setNickname(userT.getNickName());
//        userInfo.setPhone(userT.getUserPhone());
//        userInfo.setSex(userT.getUserSex());
//        userInfo.setUpdateTime(userT.getUpdateTime());
//        userInfo.setUsername(userT.getUserName());
//        userInfo.setUuid(userT.getUuid());
//        return userInfo;
//    }
}
