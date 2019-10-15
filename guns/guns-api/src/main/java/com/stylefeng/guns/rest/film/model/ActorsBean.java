package com.stylefeng.guns.rest.film.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @Description:
 * @Author: zhou
 * @Date: 2019/10/15
 * @Time 15:46
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ActorsBean implements Serializable {
    List<ActorBean> actors;
    ActorBean director;
}
