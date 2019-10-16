package com.stylefeng.guns.rest.film.service;

import com.stylefeng.guns.rest.film.model.BaseFilmResponseVO;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/12
 * @Time 17:13
 */
public interface FilmService {
    //film/getIndex
    public BaseFilmResponseVO getFilmIndex();

    public BaseFilmResponseVO<Object> searchFilmById(String info);

    public BaseFilmResponseVO<Object> searchFilmsByName(String info);
}
