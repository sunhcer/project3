package com.stylefeng.guns.rest.cinema.model;

import java.io.Serializable;

public class HallTypeVo implements Serializable {

    private static final long serialVersionUID = -7234046996146649090L;
    private String halltypeId;
    private String halltypeName;
    private boolean isActive;

    public HallTypeVo(String halltypeId, String halltypeName, boolean isActive) {
        this.halltypeId = halltypeId;
        this.halltypeName = halltypeName;
        this.isActive = isActive;
    }

    public HallTypeVo() {

    }

    public String getHalltypeId() {
        return halltypeId;
    }

    public void setHalltypeId(String halltypeId) {
        this.halltypeId = halltypeId;
    }

    public String getHalltypeName() {
        return halltypeName;
    }

    public void setHalltypeName(String halltypeName) {
        this.halltypeName = halltypeName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
