package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.film.service.SFilmService;
import com.stylefeng.guns.rest.film.vo.SFilmBaseVo;
import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;
import com.stylefeng.guns.rest.film.vo.SSelctFilmReceiveVo;
import com.stylefeng.guns.rest.film.vo.SSelectFilmVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FilmController {
    @Reference(interfaceClass = SFilmService.class)
    SFilmService filmService;
    @RequestMapping("sxg/film/getIndex")
    //影片首页测试
    public SFilmIndexPage filmGetIndex(){
        SFilmIndexPage filmBaseVo=filmService.queryFilmIndex();
        return filmBaseVo;
    }

    //
    @RequestMapping("film/getFilms")
    public SSelectFilmVo filmGetFilms(SSelctFilmReceiveVo receiveVo){
        SSelectFilmVo sSelectFilmVo=filmService.queryFilmByCondition(receiveVo);
        return sSelectFilmVo;
    }

    @RequestMapping("film/getConditionList")
    public SFilmIndexPage filmGetConditionList(SSelctFilmReceiveVo receiveVo){
        SFilmIndexPage page=filmService.queryfilmGetConditionList(receiveVo);
        return page;
    }
}
