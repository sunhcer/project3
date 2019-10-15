package com.stylefeng.guns.rest.modular.film;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.stylefeng.guns.rest.film.model.*;
import com.stylefeng.guns.rest.film.service.FilmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

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
    @Autowired
    MtimeCatDictTMapper mtimeCatDictTMapper;
    @Autowired
    MtimeSourceDictTMapper mtimeSourceDictTMapper;
    @Autowired
    MtimeFilmActorTMapper mtimeFilmActorTMapper;
    @Autowired
    MtimeActorTMapper mtimeActorTMapper;

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

    @Override
    public BaseFilmResponseVO searchFilmById(String info) {
        BaseFilmResponseVO responseVO = new BaseFilmResponseVO<>();

        MtimeFilmT mtimeFilmT = mtimeFilmTMapper.selectById(info);
        MtimeFilmInfoT mtimeFilmInfoT = filmInfoTMapper.selectById(mtimeFilmT.getUuid());
        String filmCats = mtimeFilmT.getFilmCats();
        List<String> catList = operatorCats(filmCats);
        String carts = getCatsByList(catList);

        //获取地区
        String areaName = mtimeSourceDictTMapper.selectById(mtimeFilmT.getFilmArea()).getShowName();
        String info2 = areaName + " / " + mtimeFilmInfoT.getFilmLength();

        //info3的拼接
        String info3 = mtimeFilmT.getFilmTime() + areaName + "上映";



        //查询导演和演员信息
        //导演
        MtimeActorT actorFromDb = mtimeActorTMapper.selectById(mtimeFilmInfoT.getDirectorId());

        //封装导演信息
        ActorBean actor = ActorBean.builder().imgAddress(actorFromDb.getActorImg())
                .directorName(actorFromDb.getActorName()).build();

        //查询该片的所有演员信息
        //首先查出所有的List  演员表
        EntityWrapper<MtimeFilmActorT> wrapper = new EntityWrapper<>();
        wrapper.eq("film_id", mtimeFilmT.getUuid());
        List<MtimeFilmActorT> graphActor = mtimeFilmActorTMapper.selectList(wrapper);
        ArrayList<ActorBean> actorBeans = new ArrayList<>();

        //演员表  查出信息 进行封装
        for (MtimeFilmActorT mtimeFilmActorT : graphActor) {
            MtimeActorT mtimeActorT = mtimeActorTMapper.selectById(mtimeFilmActorT.getActorId());
            ActorBean actorBean = ActorBean.builder().directorName(mtimeActorT.getActorName())
                    .imgAddress(mtimeActorT.getActorImg())
                    .roleName(mtimeFilmActorT.getRoleName()).build();
            actorBeans.add(actorBean);
        }

        ActorsBean actorsBean = new ActorsBean();
        actorsBean.setActors(actorBeans);
        actorsBean.setDirector(actor);

        String filmImgs = mtimeFilmInfoT.getFilmImgs();
        ImgsBean imgsBean = operatorImgs(filmImgs);

        //info4的拼接
        FilmInfo info4 = FilmInfo.builder().biopgraphy(mtimeFilmInfoT.getBiography())
                .filmId(mtimeFilmT.getUuid().toString())
                .actors(actorsBean)
                .imgVO(imgsBean)
                .build();

        // `film_cats` varchar(50) DEFAULT NULL COMMENT '影片分类，参照分类表,多个分类以#分割',
        SearchFilmByIdBean searchFilmByIdBean = SearchFilmByIdBean.builder()
                .filmId(mtimeFilmT.getUuid().toString())
                .filmName(mtimeFilmT.getFilmName())
                .imgAddress(mtimeFilmT.getImgAddress())
                .score(mtimeFilmT.getFilmScore())
                .scoreNum(mtimeFilmInfoT.getFilmScoreNum().toString())
                .totalBox(mtimeFilmT.getFilmBoxOffice().toString())
                .info01(carts)
                .info02(info2)
                .info03(info3)
                .info04(info4).build();

        responseVO.setData(searchFilmByIdBean);

        return responseVO;
    }

    private ImgsBean operatorImgs(String filmImgs) {
        String[] strings = filmImgs.split(",");
        ImgsBean imgsBean = new ImgsBean();

        imgsBean.setMainImg(strings[0]);
        imgsBean.setImg01(strings[1]);
        imgsBean.setImg02(strings[2]);
        imgsBean.setImg03(strings[3]);
        imgsBean.setImg04(strings[4]);
        return imgsBean;
    }

    private List<String> operatorCats(String filmCats) {
        ArrayList<String> list = new ArrayList<>();

        if (filmCats == null || filmCats.length() == 0) {
            return list;
        }
        String substring = filmCats.substring(1, filmCats.length() - 1);

        String[] split = substring.split("#");

        return Arrays.asList(split);
    }

    private String getCatsByList(List<String> catList) {
        StringBuilder sb = new StringBuilder();
        List<MtimeCatDictT> dictTList = mtimeCatDictTMapper.selectBatchIds(catList);

        for (int i = 0; i < dictTList.size(); i++) {
            sb.append(dictTList.get(i));
            if (i != dictTList.size() - 1) {
                sb.append(",");
            }
        }
        return sb.toString();
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
