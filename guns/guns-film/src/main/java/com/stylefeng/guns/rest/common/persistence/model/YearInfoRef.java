package com.stylefeng.guns.rest.common.persistence.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class YearInfoRef implements Serializable {
    boolean active;
    Object yearId;
    String yearName;
}
