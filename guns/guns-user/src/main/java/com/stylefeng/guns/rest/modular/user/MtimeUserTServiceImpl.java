package com.stylefeng.guns.rest.modular.user;


import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeUserTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeUserT;
import com.stylefeng.guns.rest.user.model.MtimeUserInfo;
import com.stylefeng.guns.rest.user.service.MtimeUserTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
        EntityWrapper<MtimeUserT> mtimeUserTEntityWrapper = new EntityWrapper<>();
        mtimeUserTEntityWrapper.eq("user_name",userName);
        mtimeUserTEntityWrapper.eq("user_pwd",password);
        List<MtimeUserT> mtimeUserTS = userTMapper.selectList(mtimeUserTEntityWrapper);
        return mtimeUserTS.size();
    }

    @Override
    public MtimeUserInfo selectUserForGatewayByUsername(String username) {
        MtimeUserT userT1 = new MtimeUserT();
        userT1.setUserName(username);
        MtimeUserT userT = userTMapper.selectOne(userT1);
        MtimeUserInfo userInfo=new MtimeUserInfo();
        userInfo.setAddress(userT.getAddress());
        userInfo.setBiography(userT.getBiography());
        userInfo.setBirthday(userT.getBirthday());
        userInfo.setEmail(userT.getEmail());
        userInfo.setCreateTime(userT.getBeginTime());
        userInfo.setHeadAdress(userT.getHeadUrl());
        userInfo.setLifeState(userT.getLifeState());
        userInfo.setNickname(userT.getNickName());
        userInfo.setPhone(userT.getUserPhone());
        userInfo.setSex(userT.getUserSex());
        userInfo.setUpdateTime(userT.getUpdateTime());
        userInfo.setUsername(userT.getUserName());
        userInfo.setUuid(userT.getUuid());
        return userInfo;
    }

    @Override
    public MtimeUserInfo selectUserInfoById(String userId) {
        MtimeUserTMapper userTMapper = this.userTMapper;
        EntityWrapper<MtimeUserT> wrapper = new EntityWrapper<>();
        wrapper.eq("uuid", userId);

        MtimeUserT userT = userTMapper.selectById(userId);
        MtimeUserInfo userInfo = new MtimeUserInfo();
        userInfo.setAddress(userT.getAddress());
        userInfo.setBiography(userT.getBiography());
        userInfo.setBirthday(userT.getBirthday());
        userInfo.setEmail(userT.getEmail());
        userInfo.setCreateTime(userT.getBeginTime());
        userInfo.setHeadAdress(userT.getHeadUrl());
        userInfo.setLifeState(userT.getLifeState());
        userInfo.setNickname(userT.getNickName());
        userInfo.setPhone(userT.getUserPhone());
        userInfo.setSex(userT.getUserSex());
        userInfo.setUpdateTime(userT.getUpdateTime());
        userInfo.setUsername(userT.getUserName());
        userInfo.setUuid(userT.getUuid());
        return userInfo;
    }

    /*@Override
    public MtimeUserT selectUserInfoById(String userId) {
        MtimeUserTMapper userTMapper = this.userTMapper;
        EntityWrapper<MtimeUserT> wrapper = new EntityWrapper<>();
        wrapper.eq("uuid", userId);

        MtimeUserT mtimeUserT = userTMapper.selectById(userId);
        return mtimeUserT;
    }*/
}
