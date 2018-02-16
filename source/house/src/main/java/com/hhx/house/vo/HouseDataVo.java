package com.hhx.house.vo;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author hhx
 * @since 2018/2/17 0:44
 */
@Data
@Builder
public class HouseDataVo {
    private List<Double> min;
    private List<Double> max;
    private List<Double> avg;

}
