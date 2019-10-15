package com.stylefeng.guns.rest.common.persistence.model;

import lombok.Data;

import java.io.Serializable;
@Data
public class SBannersRef  implements Serializable {
    Object bannerId;
    String bannerAddress;
    String bannerUrl;
}
