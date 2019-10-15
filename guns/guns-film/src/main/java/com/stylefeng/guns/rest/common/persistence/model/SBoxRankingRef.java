package com.stylefeng.guns.rest.common.persistence.model;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
@Data
public class SBoxRankingRef implements Serializable {
    int boxNum;
    int expectNum;
    String filmCats;
    Object filmId;
    Object filmLength;
    String filmName;
    String filmScore;
    int filmType;
    String imgAddress;
    String score;
    Date showTime;
}
