package com.stylefeng.guns.rest.common.persistence.model;

import java.io.Serializable;

public class SOrderBoxRef  implements Serializable {
    String orderId;
    int orderStatus;
    String orderMsg;

    public SOrderBoxRef() {
    }

    public SOrderBoxRef(String orderId, int orderStatus, String orderMsg) {
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderMsg = orderMsg;
    }

    @Override
    public String toString() {
        return "SOrderBoxRef{" +
                "orderId='" + orderId + '\'' +
                ", orderStatus=" + orderStatus +
                ", orderMsg='" + orderMsg + '\'' +
                '}';
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public int getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderMsg() {
        return orderMsg;
    }

    public void setOrderMsg(String orderMsg) {
        this.orderMsg = orderMsg;
    }
}
