package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.film.model.BaseFilmResponseVO;
import com.stylefeng.guns.rest.film.service.FilmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/14
 * @Time 17:50
 */
@RestController
public class FilmController {
    @Reference(interfaceClass = FilmService.class)
    FilmService filmService;
    ///film/getIndex
    @GetMapping("/film/getIndex")
    public BaseFilmResponseVO getFilmIndex(){
        BaseFilmResponseVO filmIndex = filmService.getFilmIndex();
        return filmIndex;
    }
}
