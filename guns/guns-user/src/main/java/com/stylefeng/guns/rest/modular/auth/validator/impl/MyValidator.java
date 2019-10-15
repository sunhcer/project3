//package com.stylefeng.guns.rest.modular.auth.validator.impl;
//
//import com.stylefeng.guns.core.util.MD5Util;
//import com.stylefeng.guns.rest.common.persistence.dao.MtimeUserTMapper;
//import com.stylefeng.guns.rest.common.persistence.model.MtimeUserT;
//import com.stylefeng.guns.rest.modular.auth.validator.IReqValidator;
//import com.stylefeng.guns.rest.modular.auth.validator.dto.Credence;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
///**
// * 类简介：
// * 当前方法：
// * 创建时间: 2019-10-14 20:58
// *
// * @author EGGE
// */
//@Service
//public class MyValidator implements IReqValidator {
//
//    @Autowired
//    MtimeUserTMapper mtimeUserTMapper;
//
//    @Override
//    public boolean validate(Credence credence) {
//        String userName = credence.getCredenceName();
//        MtimeUserT user = mtimeUserTMapper.selectUserByUserName(userName);
//        if(user==null){//没找到用户，返回失败
//            return false;
//        }
//        String password = credence.getCredenceCode();
//        String encrypt = MD5Util.encrypt(password);
//
//
//        if (user.getUserName().equals(userName) && user.getUserPwd().equals(encrypt)) {//比较密码和用户名
//            return true;
//        } else {
//            return false;
//        }
//    }
//}
