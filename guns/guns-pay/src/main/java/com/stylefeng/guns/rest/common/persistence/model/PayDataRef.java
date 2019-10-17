package com.stylefeng.guns.rest.common.persistence.model;

import java.io.Serializable;

public class PayDataRef implements Serializable {
    String orderId;
    String qRCodeAddress;

    @Override
    public String toString() {
        return "PayDataRef{" +
                "orderId='" + orderId + '\'' +
                ", qRCodeAddress='" + qRCodeAddress + '\'' +
                '}';
    }

    public PayDataRef() {
    }

    public PayDataRef(String orderId, String qRCodeAddress) {
        this.orderId = orderId;
        this.qRCodeAddress = qRCodeAddress;
    }
}
