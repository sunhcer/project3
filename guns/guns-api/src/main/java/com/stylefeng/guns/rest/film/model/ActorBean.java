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
 * @Time 15:44
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActorBean implements Serializable {
    String directorName;
    String imgAddress;
    String roleName;
}
