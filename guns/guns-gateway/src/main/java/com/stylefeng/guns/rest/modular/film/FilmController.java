package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.film.model.BaseFilmResponseVO;
import com.stylefeng.guns.rest.film.service.FilmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.stylefeng.guns.rest.film.service.SFilmService;
import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;
import com.stylefeng.guns.rest.film.vo.SSelctFilmReceiveVo;
import com.stylefeng.guns.rest.film.vo.SSelectFilmVo;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class FilmController {
    @Reference(interfaceClass = SFilmService.class,check = false)
    SFilmService sfilmService;
    @Reference(interfaceClass = FilmService.class,check=false)
    FilmService filmService;

    ///film/getIndex
    @GetMapping("/film/getIndex")
    public BaseFilmResponseVO getFilmIndex() {
        BaseFilmResponseVO filmIndex = filmService.getFilmIndex();
        return filmIndex;
    }

    //影片首页测试
    @RequestMapping("sxg/film/getIndex")
    public SFilmIndexPage filmGetIndex() {
        SFilmIndexPage filmBaseVo = sfilmService.queryFilmIndex();
        return filmBaseVo;
    }

    //
    @RequestMapping("film/getFilms")
    public SSelectFilmVo filmGetFilms(SSelctFilmReceiveVo receiveVo) {
        SSelectFilmVo sSelectFilmVo = sfilmService.queryFilmByCondition(receiveVo);
        return sSelectFilmVo;
    }

    @RequestMapping("film/getConditionList")
    public SFilmIndexPage filmGetConditionList(SSelctFilmReceiveVo receiveVo) {
        SFilmIndexPage page = sfilmService.queryfilmGetConditionList(receiveVo);
        return page;
    }
}


