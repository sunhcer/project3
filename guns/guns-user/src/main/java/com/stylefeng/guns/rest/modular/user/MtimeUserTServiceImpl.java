package com.stylefeng.guns.rest.modular.user;


import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeUserTMapper;
import com.stylefeng.guns.rest.user.model.MtimeUserInfo;
import com.stylefeng.guns.rest.user.service.MtimeUserTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 类简介：
 * 当前方法：
 * 创建时间: 2019-10-14 21:18
 *
 * @author EGGE
 */
@Component
@Service(interfaceClass = MtimeUserTService.class)
public class MtimeUserTServiceImpl implements MtimeUserTService {
    @Autowired
    MtimeUserTMapper userTMapper;
    @Override
    public Integer loginByUserNameAndPassword(String userName, String password) {
        return userTMapper.selectUserByUserNameAndPassword(userName,password);
    }

    @Override
    public MtimeUserInfo selectUserForGatewayByUsername(String username) {
        return userTMapper.selectUserInfoForGatewayByUsername(username);
    }
}
