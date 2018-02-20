package com.hhx.house.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author hhx
 * @since 2018/2/20 8:18
 */
@Data
@Builder
@AllArgsConstructor
public class LocationDataVo {
    private String name;
    private double value;
    private List<Double> coords;
}
