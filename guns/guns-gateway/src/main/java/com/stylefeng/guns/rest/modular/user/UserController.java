package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.user.model.BaseUserResponseVO;
import com.stylefeng.guns.rest.user.model.UserRegister;
import com.stylefeng.guns.rest.user.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/12
 * @Time 17:30
 */
@RestController
public class UserController {
    @Reference(interfaceClass = UserService.class,check = false)
    UserService userService;

    @RequestMapping("/user")
    public String queryUserById(Integer id){
        String username = userService.selectUserNameById(id);
        return username;
    }

    @RequestMapping("/user/check")
    public BaseUserResponseVO userCheck(String username){
        BaseUserResponseVO VO = new BaseUserResponseVO();
        int i = userService.userCheck(username);
        if(i==0){//可以注册该用户名
            VO.setStatus(0);
            VO.setMsg("用户名不存在！");
        }else if(i==1){
            VO.setStatus(1);
            VO.setMsg("用户名已注册！");
        }
        return VO;
    }

    @RequestMapping("/user/register")
    public BaseUserResponseVO userRegister(UserRegister userRegister){
        BaseUserResponseVO VO = new BaseUserResponseVO();
        int i = userService.userRegister(userRegister);
        if(i==0){//可以注册该用户名
            VO.setStatus(0);
            VO.setMsg("注册成功！");
        }else if(i==1){
            VO.setStatus(1);
            VO.setMsg("用户已经存在！");
        }
        return VO;
    }




}
