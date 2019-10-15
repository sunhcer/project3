package com.stylefeng.guns.rest.common.persistence.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
@Data
public class ConditionDataRef implements Serializable {
    List<CatInfoRef> catInfo;
    List<SourceInfoRef> sourceInfo;
    List<YearInfoRef> yearInfo;
}
