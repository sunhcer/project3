package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
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
    @Reference(interfaceClass = UserService.class)
    UserService userService;

    @RequestMapping("/user")
    public String queryUserById(Integer id){
        String username = userService.selectUserNameById(id);
        return username;
    }
}
