package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeBannerT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.rest.common.persistence.model.SBannersRef;

import java.util.List;

/**
 * <p>
 * banner信息表 Mapper 接口
 * </p>
 *
 * @author sxg
 * @since 2019-10-14
 */
public interface MtimeBannerTMapper extends BaseMapper<MtimeBannerT> {

    List<SBannersRef> queryAllBanners();
}
