package com.hhx.house.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;
@Data
@TableName("houseinfo")
public class HouseInfo {
    private String houseid;

    private String title;

    private String link;

    private String community;

    private String years;

    private String housetype;

    private String square;

    private String direction;

    private String floor;

    private String taxtype;

    private String totalprice;

    private String unitprice;

    private String followinfo;

    private String decoration;

    private Date validdate;


}