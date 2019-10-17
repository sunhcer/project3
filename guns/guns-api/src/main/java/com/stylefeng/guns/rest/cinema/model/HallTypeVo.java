package com.stylefeng.guns.rest.cinema.model;

import java.io.Serializable;

public class HallTypeVo implements Serializable {

    private static final long serialVersionUID = -7234046996146649090L;
    private Integer halltypeId;
    private String halltypeName;
    private boolean active;

    public HallTypeVo() {

    }

    public HallTypeVo(Integer halltypeId, String halltypeName, boolean active) {
        this.halltypeId = halltypeId;
        this.halltypeName = halltypeName;
        this.active = active;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getHalltypeId() {
        return halltypeId;
    }

    public void setHalltypeId(Integer halltypeId) {
        this.halltypeId = halltypeId;
    }

    public String getHalltypeName() {
        return halltypeName;
    }

    public void setHalltypeName(String halltypeName) {
        this.halltypeName = halltypeName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
