package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.core.util.MD5Util;
import com.stylefeng.guns.rest.auth.AuthRequest;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeUserTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.UserMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeUserT;
import com.stylefeng.guns.rest.common.persistence.model.User;
import com.stylefeng.guns.rest.modular.auth.validator.dto.Credence;
import com.stylefeng.guns.rest.user.model.MtimeUserInfo;
import com.stylefeng.guns.rest.user.model.UserRegister;
import com.stylefeng.guns.rest.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/12
 * @Time 17:38
 */
@Component
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {
    @Autowired
    MtimeUserTMapper mtimeUserTMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public String selectUserNameById(Integer id) {
        User user = userMapper.selectById(id);
        return user.getUserName();
    }

    @Override
    public int userCheck(String username) {
        int i = mtimeUserTMapper.selectByUserName(username);
        if (i == 0) {//没有该用户名
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int userRegister(UserRegister userRegister) {
        //先判断该表中 有没有此username
        String username = userRegister.getUsername();
        int i = mtimeUserTMapper.selectByUserName(username);
        if (i == 0) {//没有该用户名
            MtimeUserT mtimeUserT = new MtimeUserT();
            mtimeUserT.setUserName(userRegister.getUsername());
            mtimeUserT.setAddress(userRegister.getAddress());
            mtimeUserT.setBeginTime(new Date());
            mtimeUserT.setEmail(userRegister.getEmail());
            String encrypt = MD5Util.encrypt(userRegister.getPassword());
            mtimeUserT.setUserPwd(encrypt);//这个password怎么传进去
            mtimeUserT.setUserPhone(userRegister.getMobile());

//            MtimeUserT build = MtimeUserT.builder().address("465").build();
            Integer insert = mtimeUserTMapper.insert(mtimeUserT);
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public Integer updateUserInfo(MtimeUserInfo userInfo) {
        MtimeUserT user = mtimeUserTMapper.selectById(userInfo.getUuid());
        if (user == null) return 0;
        user.setAddress(userInfo.getAddress());
        user.setUserSex(userInfo.getSex());
        user.setBiography(userInfo.getBiography());
        user.setBirthday(userInfo.getBirthday());
        user.setEmail(userInfo.getEmail());
        user.setHeadUrl(userInfo.getHeadAdress());
        user.setLifeState(userInfo.getLifeState());
        user.setNickName(userInfo.getNickname());
        user.setUserName(userInfo.getUsername());
        user.setUserPhone(userInfo.getPhone());
        return mtimeUserTMapper.updateById(user);
    }

    @Override
    public boolean login(AuthRequest authRequest) {
        String userName = authRequest.getCredenceName();
        String password = authRequest.getCredenceCode();

        String md5Password = MD5Util.encrypt(password);

        EntityWrapper<MtimeUserT> wrapper = new EntityWrapper<>();
        wrapper.eq("user_name", userName);
        wrapper.eq("user_pwd", md5Password);

        List<MtimeUserT> userTList = mtimeUserTMapper.selectList(wrapper);
        if (userTList != null && userTList.size() > 0){
            //登录成功
            //将登录信息放入redis
            return true;
        }else {
            //登录失败
            return false;
        }
    }

    @Override
    public int getUserId(String username) {
        MtimeUserT mtimeUserT = mtimeUserTMapper.selectUserByUserName(username);
        return mtimeUserT.getUuid();
    }
}
