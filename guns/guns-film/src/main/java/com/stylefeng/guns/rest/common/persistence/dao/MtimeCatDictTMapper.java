package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.MtimeCatDictT;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import com.stylefeng.guns.rest.common.persistence.model.CatInfoRef;
import com.stylefeng.guns.rest.common.persistence.model.MtimeCatDictT;
import com.baomidou.mybatisplus.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 类型信息表 Mapper 接口
 * </p>
 *
 * @author zhou
 * @since 2019-10-15
 */
public interface MtimeCatDictTMapper extends BaseMapper<MtimeCatDictT> {

    List<CatInfoRef> queryqueryfilmGetConditionCatList();
}
