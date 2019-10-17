package com.stylefeng.guns.rest.cinema.model;


import java.io.Serializable;

public class CinemaQueryVo  implements Serializable {

   private static final long serialVersionUID = 8832227730036948413L;
   private Integer brandId=99;
   private Integer hallType=99;
   private Integer areaId=99;
   private Integer pageSize=12;
   private Integer nowPage=1;
   private Integer totalPage=1;

   public Integer getTotalPage() {
      return totalPage;
   }

   public void setTotalPage(Integer totalPage) {
      this.totalPage = totalPage;
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

   public Integer getHallType() {
      return hallType;
   }

   public void setHallType(Integer hallType) {
      this.hallType = hallType;
   }

   public Integer getAreaId() {
      return areaId;
   }

   public void setAreaId(Integer areaId) {
      this.areaId = areaId;
   }

   public Integer getPageSize() {
      return pageSize;
   }

   public void setPageSize(Integer pageSize) {
      this.pageSize = pageSize;
   }

   public Integer getNowPage() {
      return nowPage;
   }

   public void setNowPage(Integer nowPage) {
      this.nowPage = nowPage;
   }
}
