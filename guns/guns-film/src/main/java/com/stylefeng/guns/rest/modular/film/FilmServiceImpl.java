package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeBannerTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFilmInfoTMapper;
import com.stylefeng.guns.rest.common.persistence.dao.MtimeFilmTMapper;
import com.stylefeng.guns.rest.common.persistence.model.MtimeBannerT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmInfoT;
import com.stylefeng.guns.rest.common.persistence.model.MtimeFilmT;
import com.stylefeng.guns.rest.film.model.Banner;
import com.stylefeng.guns.rest.film.model.BaseFilmResponseVO;
import com.stylefeng.guns.rest.film.model.FilmDetail;
import com.stylefeng.guns.rest.film.model.FilmsDetail;
import com.stylefeng.guns.rest.film.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/14
 * @Time 20:38
 */
@Component
@Service(interfaceClass = FilmService.class)
public class FilmServiceImpl implements FilmService {
    @Value("${myimg.prefix}")
    String myimgPrefix;

    @Autowired
    MtimeFilmTMapper mtimeFilmTMapper;
    @Autowired
    MtimeBannerTMapper mtimeBannerTMapper;

    @Autowired
    MtimeFilmInfoTMapper filmInfoTMapper;

    @Override
    public BaseFilmResponseVO getFilmIndex() {
        BaseFilmResponseVO<Object> filmResponseVO = new BaseFilmResponseVO<>();
        //前置准备
        filmResponseVO.setImgPre(myimgPrefix);
        filmResponseVO.setMsg("");
        filmResponseVO.setNowPage(0);
        filmResponseVO.setTotalPage("");

        //查询海报信息
        ArrayList<Banner> bannerList = new ArrayList<>();
        List<MtimeBannerT> bannerTList = mtimeBannerTMapper.selectList(null);
        for (MtimeBannerT mtimeBannerT : bannerTList) {
            String bannerAddress = mtimeBannerT.getBannerAddress();
            String bannerUrl = mtimeBannerT.getBannerUrl();
            String uuid = mtimeBannerT.getUuid().toString();
            Banner banner = new Banner(bannerAddress, uuid, bannerUrl);
            bannerList.add(banner);
        }

        //查询影片信息
        EntityWrapper<MtimeFilmT> boxWrapper = new EntityWrapper<>();
        boxWrapper.orderBy("film_box_office", false);
        boxWrapper.eq("film_status", 1);

        //票房信息 根据票房查找
        FilmsDetail hotFilms = getMtimeFileByWrapper(boxWrapper);
        List<FilmDetail> boxRanking = hotFilms.getFilmInfo();

        //期待票房  根据
        EntityWrapper<MtimeFilmT> exceptWrapper = new EntityWrapper<>();
        exceptWrapper.orderBy("film_preSaleNum", false);
        exceptWrapper.eq("film_status", 2);     //影片状态
        FilmsDetail soonFilms = getMtimeFileByWrapper(exceptWrapper);
        List<FilmDetail> exceptRanking = soonFilms.getFilmInfo();


        //top100
        EntityWrapper<MtimeFilmT> top100Wrapper = new EntityWrapper<>();
        top100Wrapper.eq("film_status", 3);
        top100Wrapper.orderBy("film_score", false);
        FilmsDetail top100Film = getMtimeFileByWrapper(top100Wrapper);

        HashMap<Object, Object> map = new HashMap<>();

        map.put("banners", bannerList);
        map.put("top100", top100Film.getFilmInfo());

        map.put("boxRanking", boxRanking);
        map.put("hotFilms", hotFilms);

        map.put("exceptRanking", exceptRanking);
        map.put("soonFilms", soonFilms);



        filmResponseVO.setData(map);
        return filmResponseVO;
    }

    private FilmsDetail getMtimeFileByWrapper(EntityWrapper wrapper) {


        ArrayList<FilmDetail> filmDetailArrayList = new ArrayList<>();
        List<MtimeFilmT> mtimeFilmTS = mtimeFilmTMapper.selectList(wrapper);


        for (MtimeFilmT mtimeFilmT : mtimeFilmTS) {
            Integer filmId = mtimeFilmT.getUuid();
            MtimeFilmInfoT mtimeFilmInfoT = filmInfoTMapper.selectById(filmId);

            Integer boxNum = mtimeFilmT.getFilmBoxOffice();     //票房
            String filmCats = mtimeFilmT.getFilmCats();
            Integer filmType = mtimeFilmT.getFilmType();
            Integer exceptNum = mtimeFilmT.getFilmPresalenum();
            String fileName = mtimeFilmT.getFilmName();
            String filmScore = mtimeFilmT.getFilmScore();
            String imgAddress = mtimeFilmT.getImgAddress();
            Date showTime = mtimeFilmT.getFilmTime();

            Integer filmLength = 0;


            if (mtimeFilmInfoT != null) {
                filmLength = mtimeFilmInfoT.getFilmLength();
            }

            FilmDetail filmDetail = FilmDetail.builder().boxNum(boxNum)
                    .expectNum(exceptNum)
                    .filmCats(filmCats)
                    .filmId(filmId)
                    .filmLength(filmLength.toString())
                    .filmName(fileName)
                    .filmScore(filmScore)
                    .filmType(filmType)
                    .imgAddress(imgAddress)
                    .showTime(showTime).build();
            filmDetailArrayList.add(filmDetail);
        }
        FilmsDetail filmsDetail = new FilmsDetail();
        filmsDetail.setFilmInfo(filmDetailArrayList);

        filmsDetail.setFilmNum(mtimeFilmTS.size());
        filmsDetail.setNowPage(0);
        filmsDetail.setTotalPage(0);
        return filmsDetail;
    }
}
