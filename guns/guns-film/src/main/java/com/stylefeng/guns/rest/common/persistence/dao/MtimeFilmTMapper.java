package com.stylefeng.guns.rest.common.persistence.dao;

import com.stylefeng.guns.rest.common.persistence.model.CatInfoRef;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.stylefeng.guns.rest.common.persistence.model.SBoxRankingRef;
import com.stylefeng.guns.rest.film.vo.SSelctFilmReceiveVo;
import org.apache.ibatis.annotations.Param;
import java.util.List;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.baomidou.mybatisplus.mapper.BaseMapper;
/**
 * <p>
 * 影片主表 Mapper 接口
 * </p>
 *
 * @author sxg
 * @since 2019-10-14
 */
public interface MtimeFilmTMapper extends BaseMapper<MtimeFilmT> {

    List<SBoxRankingRef> queryFilmListByBoxNum();

    List<SBoxRankingRef> queryFilmListByExceptNum();

    List<SBoxRankingRef> queryFilmListByStatus(@Param("status") int status);

    List<SBoxRankingRef> queryFilmListByScore();

    List<SBoxRankingRef> queryFilmListByReceiveVo(@Param("receiveVo") SSelctFilmReceiveVo receiveVo,@Param("catId") String catId);

}
