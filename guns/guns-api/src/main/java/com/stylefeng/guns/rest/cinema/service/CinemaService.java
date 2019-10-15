package com.stylefeng.guns.rest.cinema.service;

import com.stylefeng.guns.rest.cinema.model.*;

import java.util.List;


public interface CinemaService {
    //接口2
    List<BrandVo> queryBrands(Integer brandId);
    List<AreaVo> queryAreas(Integer areaId);
    List<HallTypeVo> queryHallTypes(Integer hallType);
    //接口1
    List<CinemaVo> getCinemas(CinemaQueryVo cinemaQueryVo);

}
