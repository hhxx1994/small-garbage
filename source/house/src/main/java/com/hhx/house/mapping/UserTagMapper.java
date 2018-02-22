package com.hhx.house.mapping;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hhx.house.entity.UserTag;

import java.util.List;

public interface UserTagMapper extends BaseMapper<UserTag> {

    List<UserTag> getUserTagByUserId(int userId);

    List<UserTag> getUserTagExculedUserId(int userId);

}