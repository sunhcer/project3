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
public class Seat implements Serializable {
    String seatId;
    int row;
    int column;
}
