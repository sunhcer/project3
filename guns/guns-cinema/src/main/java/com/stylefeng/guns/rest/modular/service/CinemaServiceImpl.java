package com.stylefeng.guns.rest.modular.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.cinema.model.CinemaInfo;
import com.stylefeng.guns.rest.cinema.model.Film;
import com.stylefeng.guns.rest.cinema.model.FilmField;
import com.stylefeng.guns.rest.cinema.model.HallInfo;
import com.stylefeng.guns.rest.cinema.service.CinemaService;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeCinemaTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFieldTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeHallDictTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeHallFilmInfoTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeCinemaT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFieldT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeHallDictT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeHallFilmInfoT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Service(interfaceClass = CinemaService.class)
public class CinemaServiceImpl implements CinemaService {

    @Autowired
    MtimeCinemaTMapper mtimeCinemaTMapper;

    @Autowired
    MtimeFieldTMapper mtimeFieldTMapper;

    @Autowired
    MtimeHallFilmInfoTMapper mtimeHallFilmInfoTMapper;

    @Autowired
    MtimeHallDictTMapper mtimeHallDictTMapper;

    @Override
    public CinemaInfo getCinemaInfoByCinemaId(Integer cinemaId) {
        MtimeCinemaT mtimeCinemaT = mtimeCinemaTMapper.selectById(cinemaId);

        CinemaInfo cinemaInfo = CinemaInfo.builder()
                .cinemaAdress(mtimeCinemaT.getCinemaAddress())
                .cinemaId(mtimeCinemaT.getUuid())
                .cinemaName(mtimeCinemaT.getCinemaName())
                .cinemaPhone(mtimeCinemaT.getCinemaPhone())
                .imgUrl(mtimeCinemaT.getImgAddress())
                .build();

        return cinemaInfo;
    }

    @Override
    public List<Film> getFilmListByCinemaId(Integer cinemaId) {
        EntityWrapper<MtimeFieldT> wrapper = new EntityWrapper<>();
        wrapper.eq("cinema_id",cinemaId);
        List<MtimeFieldT> mtimeFieldTS = mtimeFieldTMapper.selectList(wrapper);
        ArrayList<MtimeHallFilmInfoT> mtimeHallFilmInfoTList = new ArrayList<>();
        ArrayList<Integer> integers = new ArrayList<>();

        for (MtimeFieldT mtimeFieldT : mtimeFieldTS) {
            Integer filmId = mtimeFieldT.getFilmId();
            if (!integers.contains(filmId)) {
                integers.add(filmId);
            }
        }
        for (Integer filmId : integers) {
            MtimeHallFilmInfoT mtimeHallFilmInfoT = mtimeHallFilmInfoTMapper.selectByFilmId(filmId);

            mtimeHallFilmInfoTList.add(mtimeHallFilmInfoT);
        }

        ArrayList<Film> films = new ArrayList<>();
        for (MtimeHallFilmInfoT mtimeHallFilmInfoT : mtimeHallFilmInfoTList) {

            Film film = Film.builder()
                    .filmType(mtimeHallFilmInfoT.getFilmLanguage())
                    .imgAddress(mtimeHallFilmInfoT.getImgAddress())
                    .filmName(mtimeHallFilmInfoT.getFilmName())
                    .filmLength(mtimeHallFilmInfoT.getFilmLength())
                    .filmId(mtimeHallFilmInfoT.getFilmId())
                    .filmFields(getFilmFieldsByCinemaIdAndFilmId(cinemaId, mtimeHallFilmInfoT.getFilmId()))
                    .filmCats(mtimeHallFilmInfoT.getFilmCats())
                    .actors(mtimeHallFilmInfoT.getActors())
                    .build();
            films.add(film);
        }
        return films;
    }

    @Override
    public List<FilmField> getFilmFieldsByCinemaIdAndFilmId(Integer cinemaId,Integer filmId) {
        EntityWrapper<MtimeFieldT> wrapper = new EntityWrapper<>();
        wrapper.eq("cinema_id", cinemaId);
        wrapper.eq("film_id", filmId);
        List<MtimeFieldT> mtimeFieldTS = mtimeFieldTMapper.selectList(wrapper);
        ArrayList<FilmField> filmFields = new ArrayList<>();
        for (MtimeFieldT mtimeFieldT : mtimeFieldTS) {
            FilmField filmField = new FilmField();
            filmField.setBeginTime(mtimeFieldT.getBeginTime());
            filmField.setEndTime(mtimeFieldT.getEndTime());
            filmField.setFieldId(mtimeFieldT.getUuid());
            filmField.setHallName(mtimeFieldT.getHallName());
            filmField.setPrice(mtimeFieldT.getPrice().toString());

            MtimeHallFilmInfoT mtimeHallFilmInfoT = mtimeHallFilmInfoTMapper.selectByFilmId(mtimeFieldT.getFilmId());

            filmField.setLanguage(mtimeHallFilmInfoT.getFilmLanguage());
            filmFields.add(filmField);
        }
        return filmFields;
    }

    @Override
    public HallInfo getHallInfoByFieldId(Integer fieldId) {
        MtimeFieldT mtimeFieldT = mtimeFieldTMapper.selectById(fieldId);
        MtimeHallDictT mtimeHallDictT = mtimeHallDictTMapper.selectById(mtimeFieldT.getHallId());
        String seatAddress = mtimeHallDictT.getSeatAddress();

        HallInfo hallInfo = HallInfo.builder()
                .discountPrice(null)
                .hallFieldId(mtimeFieldT.getHallId())
                .hallName(mtimeFieldT.getHallName())
                .price(mtimeFieldT.getPrice().toString())
                .seatFile(seatAddress)
                .soldSeats("3,5,7,8,9,10")
                .build();

        return hallInfo;
    }

    @Override
    public Film getFilmInfoByFieldId(Integer fieldId) {
        MtimeHallFilmInfoT mtimeHallFilmInfoT = mtimeHallFilmInfoTMapper.selectById(fieldId);
        Film film = Film.builder()
                .actors(null)
                .filmCats(mtimeHallFilmInfoT.getFilmCats())
                .filmFields(null)
                .filmId(mtimeHallFilmInfoT.getFilmId())
                .filmLength(mtimeHallFilmInfoT.getFilmLength())
                .filmName(mtimeHallFilmInfoT.getFilmName())
                .imgAddress(mtimeHallFilmInfoT.getImgAddress())
                .filmType(mtimeHallFilmInfoT.getFilmLanguage())
                .build();

        return film;
    }
}

