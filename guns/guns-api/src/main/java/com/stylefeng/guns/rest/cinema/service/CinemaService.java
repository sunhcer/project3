package com.stylefeng.guns.rest.cinema.service;

import com.stylefeng.guns.rest.cinema.model.*;
import com.stylefeng.guns.rest.cinema.model.CinemaInfo;
import com.stylefeng.guns.rest.cinema.model.Film;
import com.stylefeng.guns.rest.cinema.model.FilmField;
import com.stylefeng.guns.rest.cinema.model.HallInfo;

import java.util.List;


public interface CinemaService {
    //接口2
    List<BrandVo> queryBrands(Integer brandId);
    List<AreaVo> queryAreas(Integer areaId);
    List<HallTypeVo> queryHallTypes(Integer hallType);
    //接口1
    List<CinemaVo> getCinemas(CinemaQueryVo cinemaQueryVo);


    CinemaInfo getCinemaInfoByCinemaId(Integer cinemaId);
    List<Film> getFilmListByCinemaId(Integer cinemaId);
    List<FilmField> getFilmFieldsByCinemaIdAndFilmId(Integer cinemaId, Integer filmId);
    HallInfo getHallInfoByFieldId(Integer fieldId);
    Film getFilmInfoByFieldId(Integer fieldId);
}
