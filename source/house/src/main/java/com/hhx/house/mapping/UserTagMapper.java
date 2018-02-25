package com.hhx.house.mapping;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hhx.house.entity.UserTag;
import com.hhx.house.model.TrainData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserTagMapper extends BaseMapper<UserTag> {

    List<UserTag> getUserTagByUserId(int userId);

    List<UserTag> getUserTagExculedUserId(int userId);

    void insertBatch(@Param("userTags") List<UserTag> userTags);

    List<TrainData> getTrainData();
}