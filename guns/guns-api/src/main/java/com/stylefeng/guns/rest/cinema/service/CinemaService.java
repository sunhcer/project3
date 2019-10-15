package com.stylefeng.guns.rest.cinema.service;

import com.stylefeng.guns.rest.cinema.model.CinemaInfo;
import com.stylefeng.guns.rest.cinema.model.Film;
import com.stylefeng.guns.rest.cinema.model.FilmField;

import java.util.List;

public interface CinemaService {

    CinemaInfo getCinemaInfoByCinemaId(Integer cinemaId);

    List<Film> getFilmListByCinemaId(Integer cinemaId);

    List<FilmField> getFilmFieldsByCinemaId(Integer cinemaId, Integer filmId);

}
