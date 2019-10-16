package com.stylefeng.guns.rest.film.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/15
 * @Time 15:51
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImgsBean implements Serializable {
    String img01;
    String img02;
    String img03;
    String img04;
    String mainImg;
}
