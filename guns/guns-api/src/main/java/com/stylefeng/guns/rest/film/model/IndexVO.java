package com.stylefeng.guns.rest.film.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class IndexVO implements Serializable {
    List<Banner> banners;
    List boxRanking;
    List expectRanking;
    FilmsDetail hotFilms;
    FilmsDetail soonFilms;
    List<FilmDetail> top100;
}
