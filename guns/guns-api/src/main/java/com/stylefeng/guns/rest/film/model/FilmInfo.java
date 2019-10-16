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
 * @Time 15:42
 */
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FilmInfo implements Serializable {
    ActorsBean actors;
    String biopgraphy;
    String filmId;
    ImgsBean imgVO;

}
