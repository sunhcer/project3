package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Reference;
import com.google.common.util.concurrent.RateLimiter;
import com.stylefeng.guns.rest.film.model.BaseFilmResponseVO;
import com.stylefeng.guns.rest.film.service.FilmService;
import com.stylefeng.guns.rest.film.service.SFilmService;
import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;
import com.stylefeng.guns.rest.film.vo.SSelctFilmReceiveVo;
import com.stylefeng.guns.rest.film.vo.SSelectFilmVo;
import com.stylefeng.guns.rest.modular.cache.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@RestController
public class FilmController {
    @Reference(interfaceClass = SFilmService.class,check = false)
    SFilmService sfilmService;
    @Reference(interfaceClass = FilmService.class,check=false)
    FilmService filmService;
    @Autowired
    CacheService cacheService;
    @Autowired
    private RedisTemplate redisTemplate;
    RateLimiter rateLimiter;

    @PostConstruct
    public void init(){
        rateLimiter = RateLimiter.create(30);
    }

    ///film/getIndex
    @GetMapping("/film/getIndex")
    public BaseFilmResponseVO getFilmIndex() {
        Object filmIndexFromCache = cacheService.get("filmIndex");
        Object filmIndexFromRedis = redisTemplate.opsForValue().get("filmIndex");
        if(filmIndexFromCache != null || filmIndexFromRedis != null){
            if (filmIndexFromCache != null) {
                BaseFilmResponseVO fromCache = (BaseFilmResponseVO) filmIndexFromCache;
                redisTemplate.opsForValue().set("filmIndex", fromCache);
                redisTemplate.expire("filmIndex", 5, TimeUnit.MINUTES);
                return fromCache;
            }else{
                BaseFilmResponseVO fromRedis = (BaseFilmResponseVO) filmIndexFromRedis;
                cacheService.set("filmIndex", fromRedis);
                return fromRedis;
            }
        }

        //两层缓存都失效了  获取桶  如果桶不够 拒绝掉它(请求)
        double acquire = rateLimiter.acquire();
        if (acquire < 0){
            return BaseFilmResponseVO.fail("获取首页失败,请重试");
        }

        BaseFilmResponseVO filmIndex = filmService.getFilmIndex();
        filmIndex.setStatus(0);
        //将主页放入缓存中
        cacheService.set("filmIndex", filmIndex);
        redisTemplate.opsForValue().set("filmIndex", filmIndex);
        redisTemplate.expire("filmIndex", 5, TimeUnit.MINUTES);

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


