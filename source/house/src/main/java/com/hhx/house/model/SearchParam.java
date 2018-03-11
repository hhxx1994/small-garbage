package com.hhx.house.model;

import lombok.Data;

import java.util.List;

/**
 * @author hhx
 * @since 2018/3/11 15:55
 */
@Data
public class SearchParam {

    private String name;
    private List<Double> unitPrice;
    private List<Double> area;
    private List<Integer> year;


}
