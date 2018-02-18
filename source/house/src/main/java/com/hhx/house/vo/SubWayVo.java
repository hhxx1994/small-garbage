package com.hhx.house.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author hhx
 * @since 2018/2/19 0:08
 */
@Data
@Builder
public class SubWayVo {
    private double upper;
    private double q3;
    private double median;
    private double q1;
    private double lower;
}
