package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MoocOrderT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.rest.pay.model.QROrderRef;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 订单信息表 Mapper 接口
 * </p>
 *
 * @author sxg
 * @since 2019-10-16
 */
public interface MoocOrderTMapper extends BaseMapper<MoocOrderT> {

    void updateOrderStatusBySandBox(@Param("orderId") String orderId);

    QROrderRef queryOrderPrice(@Param("orderId")String orderId);
}
