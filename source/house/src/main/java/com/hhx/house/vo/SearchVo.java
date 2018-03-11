package com.hhx.house.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author hhx
 * @since 2018/3/11 18:37
 */
@Data
@Builder
public class SearchVo {

    private String houseId;
    private String title;
    private String community;
    private Integer year;
    private Integer floor;
    private Integer room;
    private Integer office;
    private Double square;
    private Double totalPrice;
    private Double unitPrice;
    private Integer focus;
    private Integer watch;
    private Double lat;
    private Double lng;


}
