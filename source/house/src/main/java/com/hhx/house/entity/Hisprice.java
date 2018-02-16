package com.hhx.house.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("hisprice")
public class Hisprice {
    private String houseid;

    private String totalprice;

    private Date date;

}