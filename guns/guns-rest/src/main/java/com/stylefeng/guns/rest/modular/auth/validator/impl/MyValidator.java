package com.stylefeng.guns.rest.modular.auth.validator.impl;

import com.stylefeng.guns.rest.common.persistence.dao.MtimeUserTMapper;
import com.stylefeng.guns.rest.modular.auth.validator.IReqValidator;
import com.stylefeng.guns.rest.modular.auth.validator.dto.Credence;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 类简介：
 * 当前方法：
 * 创建时间: 2019-10-14 20:58
 *
 * @author EGGE
 */
public class MyValidator implements IReqValidator {

    @Autowired
    MtimeUserTMapper mtimeUserTMapper;

    @Override
    public boolean validate(Credence credence) {
        String userName = credence.getCredenceName();
        String password = credence.getCredenceCode();


        if ("".equals(userName) && "".equals(password)) {
            return true;
        } else {
            return false;
        }
    }
}
