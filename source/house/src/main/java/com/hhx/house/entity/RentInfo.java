package com.hhx.house.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;
@Data
@TableName("rentinfo")
public class RentInfo {
    private String houseid;

    private String title;

    private String link;

    private String region;

    private String zone;

    private String meters;

    private String other;

    private String subway;

    private String decoration;

    private String heating;

    private String price;

    private String pricepre;

    private Date updatedate;


}