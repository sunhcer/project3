package com.stylefeng.guns.rest.modular.cinema;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.cinema.model.CinemaInfo;
import com.stylefeng.guns.rest.cinema.model.FieldsVO;
import com.stylefeng.guns.rest.cinema.model.Film;
import com.stylefeng.guns.rest.cinema.service.CinemaService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CinemaController {

    @Reference(interfaceClass = CinemaService.class,check = false)
    CinemaService cinemaService;

    @RequestMapping("/cinema/getFields")
    public FieldsVO getFields(Integer cinemaId) {

        FieldsVO fieldsVO = new FieldsVO();

        CinemaInfo cinemaInfo = cinemaService.getCinemaInfoByCinemaId(cinemaId);

        List<Film> filmList = cinemaService.getFilmListByCinemaId(cinemaId);

        fieldsVO.setCinemaInfo(cinemaInfo);
        fieldsVO.setFilmList(filmList);
        fieldsVO.setStatus(0);

        return fieldsVO;
    }
}
