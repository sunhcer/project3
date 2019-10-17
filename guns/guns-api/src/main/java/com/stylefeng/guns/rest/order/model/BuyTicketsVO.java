package com.stylefeng.guns.rest.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BuyTicketsVO implements Serializable {
    private Integer fieldId;
    private String soldSeats;
    private String seatsName;
}
