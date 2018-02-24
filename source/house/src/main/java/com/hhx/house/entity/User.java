package com.hhx.house.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("user")
public class User {
    private Integer id;

    private String name;

    private String password;

    private Date registerDate;

    private Integer status;

}