package com.hhx.house.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author hhx
 * @since 2018/2/19 10:50
 */
@Data
@Builder
@AllArgsConstructor
public class YearPriceVo {
    private int year;
    private double price;
}
