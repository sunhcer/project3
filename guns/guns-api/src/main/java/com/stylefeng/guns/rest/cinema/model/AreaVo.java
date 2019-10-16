package com.stylefeng.guns.rest.cinema.model;

import java.io.Serializable;

public class AreaVo implements Serializable {

    private static final long serialVersionUID = 1187501332204861514L;
    private String areaId;
   private String areaName;
   private boolean isActive;

    public AreaVo() {

    }
    public AreaVo(String areaId, String areaName, boolean isActive) {
        this.areaId = areaId;
        this.areaName = areaName;
        this.isActive = isActive;
    }
    public String getAreadId() {
        return areaId;
    }
    public void setAreadId(String areaId) {
        this.areaId = areaId;
    }
    public String getAreaName() {
        return areaName;
    }
    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }
    public boolean isActive() {
        return isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}
