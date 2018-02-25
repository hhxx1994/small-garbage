package com.hhx.house.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("house_img")
public class HouseImg {
    private String houseId;

    private String img;

}