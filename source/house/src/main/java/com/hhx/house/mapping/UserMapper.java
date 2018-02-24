package com.hhx.house.mapping;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hhx.house.entity.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper  extends BaseMapper<User>{

    int insertAndGetId(@Param("user") User user);

}