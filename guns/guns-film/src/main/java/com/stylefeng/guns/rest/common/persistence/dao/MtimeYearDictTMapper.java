package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeYearDictT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.rest.common.persistence.model.YearInfoRef;

import java.util.List;

/**
 * <p>
 * 年代信息表 Mapper 接口
 * </p>
 *
 * @author sxg
 * @since 2019-10-15
 */
public interface MtimeYearDictTMapper extends BaseMapper<MtimeYearDictT> {

    List<YearInfoRef> queryqueryfilmGetConditionYearList();
}
