package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.film.model.BaseFilmResponseVO;
import com.stylefeng.guns.rest.film.service.FilmService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/14
 * @Time 17:50
 */
@RestController
public class FilmController {
    @Reference(interfaceClass = FilmService.class, check = false)
    FilmService filmService;
    ///film/getIndex
    @GetMapping("/film/getIndex")
    public BaseFilmResponseVO getFilmIndex(){
        BaseFilmResponseVO filmIndex = filmService.getFilmIndex();
        return filmIndex;
    }

    ///film/films/{影片编号或影片名称}
    @GetMapping(value = "/film/films/{info}")
    public BaseFilmResponseVO<Object> getFilmInfo(Integer searchType, @PathVariable("info")String info){
        BaseFilmResponseVO<Object> responseVO = new BaseFilmResponseVO<>();
        if (searchType == 0){
            //按照编号找
            responseVO = filmService.searchFilmById(info);
        }else{
            //按照名称找
            responseVO = filmService.searchFilmById(info);
        }
        return responseVO;
    }
}
