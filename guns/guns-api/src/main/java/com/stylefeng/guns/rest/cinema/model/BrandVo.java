package com.stylefeng.guns.rest.cinema.model;

import java.io.Serializable;

public class BrandVo implements Serializable {

    private static final long serialVersionUID = -9099102863266367667L;
    private String brandId;
    private  String brandName;
    private boolean isActive;

    public BrandVo(String brandId, String brandName, boolean isActive) {
        this.brandId = brandId;
        this.brandName = brandName;
        this.isActive = isActive;
    }

    public BrandVo() {

    }

    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
