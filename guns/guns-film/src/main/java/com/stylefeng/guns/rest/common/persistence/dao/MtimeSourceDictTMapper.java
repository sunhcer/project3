package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeSourceDictT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.rest.common.persistence.model.SourceInfoRef;

import java.util.List;

/**
 * <p>
 * 区域信息表 Mapper 接口
 * </p>
 *
 * @author sxg
 * @since 2019-10-15
 */
public interface MtimeSourceDictTMapper extends BaseMapper<MtimeSourceDictT> {

    List<SourceInfoRef> queryqueryfilmGetConditionSourceList();
}
