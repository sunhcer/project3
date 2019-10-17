package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.stylefeng.guns.rest.film.model.BaseFilmResponseVO;
import com.stylefeng.guns.rest.film.service.FilmService;
import com.stylefeng.guns.rest.film.service.SFilmService;
import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;
import com.stylefeng.guns.rest.film.vo.SSelctFilmReceiveVo;
import com.stylefeng.guns.rest.film.vo.SSelectFilmVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
        filmIndex.setStatus(0);
        return filmIndex;
    }

    ///film/films/{影片编号或影片名称}
    @GetMapping(value = "/film/films/{info}")
    public BaseFilmResponseVO<Object> getFilmInfo(Integer searchType, @PathVariable("info")String info) {
        BaseFilmResponseVO<Object> responseVO = new BaseFilmResponseVO<>();
        try {
            if (searchType == 0) {
                //按照编号找
                responseVO = filmService.searchFilmById(info);
            } else {
                //按照名称找
                responseVO = filmService.searchFilmsByName(info);
            }
        }catch (Exception e){
//            throw new GunsException(BizExceptionEnum.SYSTEM_EXCEPTION);
            responseVO.setStatus(999);
            responseVO.setCode(999);
            responseVO.setMsg("系统出现异常，请联系管理员");
            return responseVO;
        }

        if(responseVO.getData() == null){
//            throw new GunsException(BizExceptionEnum.FILM_EMPTY_EXCEPTION);
            responseVO.setStatus(999);
            responseVO.setCode(999);
            responseVO.setMsg("系统出现异常，请联系管理员");
            return responseVO;
        }

        return responseVO;
    }
    //影片首页测试
    @RequestMapping("sxg/film/getIndex")
    public SFilmIndexPage filmGetIndex(){
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


