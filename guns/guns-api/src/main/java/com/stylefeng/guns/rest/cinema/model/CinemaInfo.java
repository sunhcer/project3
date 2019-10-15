package com.stylefeng.guns.rest.cinema.model;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class CinemaInfo implements Serializable {

    private String cinemaAdress;

    private Integer cinemaId;

    private String cinemaName;

    private String cinemaPhone;

    private String imgUrl;

   /* public CinemaInfo(String cinemaAdress, Integer cinemaId, String cinemaName, String cinemaPhone, String imgUrl) {
        this.cinemaAdress = cinemaAdress;
        this.cinemaId = cinemaId;
        this.cinemaName = cinemaName;
        this.cinemaPhone = cinemaPhone;
        this.imgUrl = imgUrl;
    }

    public CinemaInfo() {
    }

    public String getCinemaAdress() {
        return cinemaAdress;
    }

    public void setCinemaAdress(String cinemaAdress) {
        this.cinemaAdress = cinemaAdress;
    }

    public Integer getCinemaId() {
        return cinemaId;
    }

    public void setCinemaId(Integer cinemaId) {
        this.cinemaId = cinemaId;
    }

    public String getCinemaName() {
        return cinemaName;
    }

    public void setCinemaName(String cinemaName) {
        this.cinemaName = cinemaName;
    }

    public String getCinemaPhone() {
        return cinemaPhone;
    }

    public void setCinemaPhone(String cinemaPhone) {
        this.cinemaPhone = cinemaPhone;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }*/
}
