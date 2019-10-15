package com.stylefeng.guns.rest.user.service;

import com.stylefeng.guns.rest.auth.AuthRequest;
import com.stylefeng.guns.rest.user.model.MtimeUserInfo;
import com.stylefeng.guns.rest.user.model.UserRegister;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/12
 * @Time 17:12
 */
public interface UserService {
    String selectUserNameById(Integer id);

    int userCheck(String username);

    int userRegister(UserRegister userRegister);

    Integer updateUserInfo(MtimeUserInfo userInfo);

    boolean login(AuthRequest authRequest);

    int getUserId(String username);
}
