package com.hhx.house.dto;

import lombok.Data;

/**
 * @author hhx
 * @since 2018/2/20 11:23
 */
@Data
public class LocationDataDto {
    private double price;
    private String community;
    private double gcjLng;
    private double gcjLat;
    private String houseId;
}
