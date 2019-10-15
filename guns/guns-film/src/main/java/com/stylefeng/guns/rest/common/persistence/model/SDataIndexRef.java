package com.stylefeng.guns.rest.common.persistence.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class SDataIndexRef implements Serializable {
    List<SBannersRef> banners;
    List<SBoxRankingRef> boxRanking;
    List<SBoxRankingRef> expectRanking;
    SHotFilmsRef  hotFilms;
    SHotFilmsRef soonFilms;
    List<SBoxRankingRef> top100;
}
