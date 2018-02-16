package com.hhx.house.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("maplocation")
public class MapLocation {
    private Long id;

    private String data;

    private String lng;

    private String lat;

    private String precise;

    private String confidence;

    private String level;

    private String name;

    private Integer status;

    private String gcjLng;

    private String gcjLat;


}