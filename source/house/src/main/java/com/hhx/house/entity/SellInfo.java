package com.hhx.house.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("sellinfo")
public class SellInfo {
    private String houseid;

    private String title;

    private String link;

    private String community;

    private String years;

    private String housetype;

    private String square;

    private String direction;

    private String floor;

    private String status;

    private String source;

    private String totalprice;

    private String unitprice;

    private String dealdate;

    private Date updatedate;

}