package com.hhx.house.entity;

import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

@Data
@TableName("usertag")
public class UserTag {
    private Integer id;

    private Integer userId;

    private Integer houseId;


}