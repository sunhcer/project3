package com.stylefeng.guns.rest.order.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeatsInfo implements Serializable {
    int limit;
//    List<String> ids;
    Object ids;
    List<List<Seat>> single;
    List<List<Seat>> couple;
}
