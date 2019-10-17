package com.stylefeng.guns.rest.common.persistence.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class SHotFilmsRef implements Serializable {
    List<SBoxRankingRef> filmInfo;
    Object filmNum;
    String nowPage;
    String totalPage;
}
