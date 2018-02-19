package com.hhx.house.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author hhx
 * @since 2018/2/19 9:57
 */
@Data
@Builder
@AllArgsConstructor
public class PeoplePriceVo {
    private int houseNum;
    private double price;
}
