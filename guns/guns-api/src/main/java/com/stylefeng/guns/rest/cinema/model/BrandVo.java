package com.stylefeng.guns.rest.cinema.model;

import java.io.Serializable;

public class BrandVo implements Serializable {

    private static final long serialVersionUID = -9099102863266367667L;
    private Integer brandId;
    private  String brandName;
    private boolean active;

    public BrandVo() {

    }

    public BrandVo(Integer brandId, String brandName, boolean active) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.active = active;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
