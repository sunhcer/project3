package com.stylefeng.guns.rest.cinema.model;

import java.io.Serializable;

public class AreaVo implements Serializable {

    private static final long serialVersionUID = 1187501332204861514L;
    private Integer areaId;
    private String areaName;
    private boolean active;

    public AreaVo() {

    }

    public AreaVo(Integer areaId, String areaName, boolean active) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.active = active;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
