package com.stylefeng.guns.rest.common.persistence.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class CatInfoRef implements Serializable {
    boolean active;
    Object catId;
    String catName;
}
