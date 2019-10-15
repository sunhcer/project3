package com.stylefeng.guns.rest.modular.user;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.user.model.BaseVo;
import com.stylefeng.guns.rest.user.model.MtimeUserInfo;
import com.stylefeng.guns.rest.user.service.MtimeUserTService;
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

    @Reference(interfaceClass = MtimeUserTService.class,check = false)
    MtimeUserTService mtimeUserTService;

    @Reference(interfaceClass = UserService.class,check = false)
    UserService userService;

    @RequestMapping("/user")
    public String queryUserById(Integer id) {
        String username = userService.selectUserNameById(id);
        return username;
    }

    /*    @RequestMapping("test")
        public String test(){
            return "test";
        }*/
   /* @RequestMapping("auth")
    public String userLogin(String userName,String password) {

        if(mtimeUserTService.loginByUserNameAndPassword(userName,password)==1){
            return 1+"";
        }

        return 0+"";
    }*/

    @RequestMapping("/user/getUserInfo")
    public BaseVo getUserInfoByToken(){
       MtimeUserInfo userInfo = mtimeUserTService.selectUserForGatewayByUsername("admin");
       return BaseVo.successVo(userInfo,null);
    }


}
