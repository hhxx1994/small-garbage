package com.hhx.house.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("community")
public class Community {
    private Long id;

    private String title;

    private String link;

    private String district;

    private String bizcircle;

    private String taglist;

    private String onsale;

    private String onrent;

    private String year;

    private String housetype;

    private String cost;

    private String service;

    private String company;

    private String buildingNum;

    private String houseNum;

    private String price;

    private Date validdate;

}