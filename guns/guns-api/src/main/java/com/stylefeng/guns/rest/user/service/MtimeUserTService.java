package com.stylefeng.guns.rest.user.service;

import com.stylefeng.guns.rest.user.model.MtimeUserInfo;

/**
 * 类简介：
 * 当前方法：
 * 创建时间: 2019-10-14 21:18
 *
 * @author EGGE
 */
public interface MtimeUserTService {
    Integer loginByUserNameAndPassword(String userName, String password);

    MtimeUserInfo selectUserForGatewayByUsername(String username);

    MtimeUserInfo selectUserInfoById(String userId);

}
