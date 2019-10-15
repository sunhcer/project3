package com.stylefeng.guns.rest.common.persistence.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class SourceInfoRef  implements Serializable {
    boolean active;
    Object sourceId;
    String sourceName;
}
