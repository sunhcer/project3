package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.cinema.model.*;
import com.stylefeng.guns.rest.cinema.service.CinemaService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CinemaController {

    @Value("${imgPre.path}")
    String imgPre;

    @Reference(interfaceClass = CinemaService.class,check = false)
    CinemaService cinemaService;

    @RequestMapping("/cinema/getFields")
    public BaseResultVO getFields(Integer cinemaId) {

        CinemaInfo cinemaInfo = cinemaService.getCinemaInfoByCinemaId(cinemaId);

        List<Film> filmList = cinemaService.getFilmListByCinemaId(cinemaId);

        FieldsVO fieldsVO = FieldsVO.builder()
                .cinemaInfo(cinemaInfo)
                .filmList(filmList)
                .build();

        BaseResultVO baseResultVO = BaseResultVO.builder()
                .data(fieldsVO)
                .imgPre(imgPre)
                .status(0)
                .msg(null)
                .nowPage(null)
                .totalPage(null)
                .build();

        return baseResultVO;
    }

    @RequestMapping("/cinema/getFieldInfo")
    public BaseResultVO getFieldInfo(Integer cinemaId, Integer fieldId) {
        HallInfoVO hallInfoVO = HallInfoVO.builder()
                .cinemaInfo(cinemaService.getCinemaInfoByCinemaId(cinemaId))
                .hallInfo(cinemaService.getHallInfoByFieldId(fieldId))
                .filmInfo(cinemaService.getFilmInfoByFieldId(fieldId))
                .build();

        BaseResultVO baseResultVO = BaseResultVO.builder()
                .data(hallInfoVO)
                .imgPre(imgPre)
                .status(0)
                .msg(null)
                .nowPage(null)
                .totalPage(null)
                .build();

        return baseResultVO;
    }
}
