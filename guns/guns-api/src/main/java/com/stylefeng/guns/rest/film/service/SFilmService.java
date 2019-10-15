package com.stylefeng.guns.rest.film.service;

import com.stylefeng.guns.rest.film.vo.SFilmBaseVo;
import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;
import com.stylefeng.guns.rest.film.vo.SSelctFilmReceiveVo;
import com.stylefeng.guns.rest.film.vo.SSelectFilmVo;

public interface SFilmService {

    SFilmIndexPage queryFilmIndex();

    SSelectFilmVo queryFilmByCondition(SSelctFilmReceiveVo receiveVo);

    SFilmIndexPage queryfilmGetConditionList(SSelctFilmReceiveVo receiveVo);
}
