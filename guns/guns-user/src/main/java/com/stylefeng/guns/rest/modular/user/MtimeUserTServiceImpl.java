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
        if(userT!=null)
        return getMtimeUserInfo(userT);
        return null;
    }

    private MtimeUserInfo getMtimeUserInfo(MtimeUserT userT) {
        MtimeUserInfo userInfo=new MtimeUserInfo();
        userInfo.setAddress(userT.getAddress());
        userInfo.setBiography(userT.getBiography());
        userInfo.setBirthday(userT.getBirthday());
        userInfo.setEmail(userT.getEmail());
        if(userT.getBeginTime()!=null)
        userInfo.setCreateTime(userT.getBeginTime().toString());
        userInfo.setHeadAdress(userT.getHeadUrl());
        if(userT.getLifeState()!=null)
        userInfo.setLifeState(userT.getLifeState().toString());
        userInfo.setNickname(userT.getNickName());
        userInfo.setPhone(userT.getUserPhone());
        if(userT.getUserSex()!=null)
        userInfo.setSex(userT.getUserSex().toString());
        if(userT.getUpdateTime()!=null)
        userInfo.setUpdateTime(userT.getUpdateTime().toString());
        userInfo.setUsername(userT.getUserName());
        if(userT.getUuid()!=null)
        userInfo.setUuid(userT.getUuid().toString());
        return userInfo;
    }

    @Override
    public MtimeUserInfo selectUserInfoById(String userId) {
        MtimeUserTMapper userTMapper = this.userTMapper;
        EntityWrapper<MtimeUserT> wrapper = new EntityWrapper<>();
        wrapper.eq("uuid", userId);
        List<MtimeUserT> mtimeUserTS = userTMapper.selectList(wrapper);
        MtimeUserT userT = new MtimeUserT();
        if(mtimeUserTS!=null&&mtimeUserTS.size()>0)
        userT=mtimeUserTS.get(0);
        if(userT==null)
            return null;
        return getMtimeUserInfo(userT);
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
