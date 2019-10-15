package com.stylefeng.guns.rest.film.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/14
 * @Time 20:50
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Banner implements Serializable {
    String bannerAddress;
    String bannerId;
    String bannerUrl;
}
