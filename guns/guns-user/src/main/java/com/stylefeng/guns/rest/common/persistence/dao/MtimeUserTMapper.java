package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeUserT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.rest.user.model.MtimeUserInfo;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author wotainanl
 * @since 2019-10-14
 */
public interface MtimeUserTMapper extends BaseMapper<MtimeUserT> {

    MtimeUserInfo selectUserInfoForGatewayByUsername(String username);

    Integer selectUserByUserNameAndPassword(@Param("userName") String userName, @Param("password") String password);

    int selectByUserName(@Param("username")String username);


}
