package com.hhx.house.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author hhx
 * @since 2018/2/20 17:06
 */
@Data
@Builder
public class CommunityPrice {
    private String name;
    private double price;
}
