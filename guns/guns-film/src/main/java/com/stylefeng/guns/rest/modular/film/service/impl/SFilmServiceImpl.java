package com.stylefeng.guns.rest.modular.film.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.stylefeng.guns.rest.common.persistence.dao.*;
import com.stylefeng.guns.rest.common.persistence.model.*;
import com.stylefeng.guns.rest.film.service.SFilmService;
import com.stylefeng.guns.rest.film.vo.SFilmIndexPage;
import com.stylefeng.guns.rest.film.vo.SSelctFilmReceiveVo;
import com.stylefeng.guns.rest.film.vo.SSelectFilmVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Service(interfaceClass = SFilmService.class)
public class SFilmServiceImpl implements SFilmService {
    @Autowired
    MtimeBannerTMapper bannerTMapper;
    @Autowired
    MtimeFilmTMapper filmTMapper;
    @Autowired
    MtimeCatDictTMapper catDictTMapper;
    @Autowired
    MtimeSourceDictTMapper sourceDictTMapper;
    @Autowired
    MtimeYearDictTMapper yearDictTMapper;
    @Override
    public SFilmIndexPage queryFilmIndex() {
        List<SBannersRef> banners=bannerTMapper.queryAllBanners();
        List<SBoxRankingRef> boxRanking=filmTMapper.queryFilmListByBoxNum();
        List<SBoxRankingRef> expectRanking=filmTMapper.queryFilmListByExceptNum();
       List<SBoxRankingRef> hotFilms=filmTMapper.queryFilmListByStatus(1);
        SHotFilmsRef hotFilmsRef = new SHotFilmsRef();
        hotFilmsRef.setFilmInfo(hotFilms);
        hotFilmsRef.setFilmNum(hotFilms.size());
        hotFilmsRef.setNowPage("");
        hotFilmsRef.setTotalPage("");

        List<SBoxRankingRef> filmInfo=filmTMapper.queryFilmListByStatus(2);
        SHotFilmsRef soonFilmsRef1 = new SHotFilmsRef();
        soonFilmsRef1.setFilmNum(filmInfo.size());
        soonFilmsRef1.setFilmInfo(filmInfo);
        List<SBoxRankingRef> topHudersd=filmTMapper.queryFilmListByScore();
        SDataIndexRef sDataIndexRef = new SDataIndexRef();
        sDataIndexRef.setBanners(banners);
        sDataIndexRef.setBoxRanking(boxRanking);
        sDataIndexRef.setExpectRanking(expectRanking);
        sDataIndexRef.setSoonFilms(soonFilmsRef1);
        sDataIndexRef.setHotFilms(hotFilmsRef);
        sDataIndexRef.setTop100(topHudersd);
        SFilmIndexPage<SDataIndexRef> sDataIndexRefSFilmIndexPage = new SFilmIndexPage<>();
        sDataIndexRefSFilmIndexPage.setData(sDataIndexRef);
        sDataIndexRefSFilmIndexPage.setImgPre("http://img.meetingshop.cn/");
        sDataIndexRefSFilmIndexPage.setStatus(0);
        return sDataIndexRefSFilmIndexPage;
    }

    @Override
    public SSelectFilmVo queryFilmByCondition(SSelctFilmReceiveVo receiveVo) {

        //模糊查询拼接#xxx#
        int cId=receiveVo.getCatId();
        String catId="#"+cId+"#";
        List<SBoxRankingRef> data=filmTMapper.queryFilmListByReceiveVo(receiveVo,catId);
        SSelectFilmVo sSelectFilmVo = new SSelectFilmVo<>();
        sSelectFilmVo.setData(data);
        sSelectFilmVo.setImgPre("http://img.meetingshop.cn/");
        //根据偏移量得到当前页数
        int pageSize=receiveVo.getPageSize();
        int nowPage=0;
        int offset=receiveVo.getOffset();
        if (offset<=pageSize) {
            sSelectFilmVo.setNowPage(1);
        }else if (pageSize!=0&&offset%pageSize==0){
            nowPage=offset/pageSize;
        }else if (pageSize!=0){
            nowPage=offset/pageSize+1;
        }
        sSelectFilmVo.setNowPage(nowPage);
        //得到总页数
        sSelectFilmVo.setTotalPage(0);
        return sSelectFilmVo;
    }

    @Override
    public SFilmIndexPage queryfilmGetConditionList(SSelctFilmReceiveVo receiveVo) {
        //找出所有的,前端自己会判断,99为true
        List<CatInfoRef> catInfoRefs=catDictTMapper.queryqueryfilmGetConditionCatList();
        for (CatInfoRef catInfoRef : catInfoRefs) {
            if (catInfoRef.getCatId().equals(99)){
                catInfoRef.setActive(true);
            }
        }
        List<SourceInfoRef> sourceInfoRefs=sourceDictTMapper.queryqueryfilmGetConditionSourceList();
        for (SourceInfoRef sourceInfoRef : sourceInfoRefs) {
            if (sourceInfoRef.getSourceId().equals(99)){
                sourceInfoRef.setActive(true);
            }
        }
        List<YearInfoRef> yearInfoRefs=yearDictTMapper.queryqueryfilmGetConditionYearList();
        for (YearInfoRef yearInfoRef : yearInfoRefs) {
            if (yearInfoRef.getYearId().equals(99)){
                yearInfoRef.setActive(true);
            }
        }
        ConditionDataRef conditionDataRef = new ConditionDataRef();
        conditionDataRef.setCatInfo(catInfoRefs);
        conditionDataRef.setSourceInfo(sourceInfoRefs);
        conditionDataRef.setYearInfo(yearInfoRefs);
        SFilmIndexPage<ConditionDataRef> sFilmIndexPage = new SFilmIndexPage<>();
        sFilmIndexPage.setData(conditionDataRef);
        sFilmIndexPage.setStatus(0);
        return sFilmIndexPage;
    }
}
