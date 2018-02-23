package com.hhx.house.mapping;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hhx.house.entity.HouseInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface HouseInfoMapper extends BaseMapper<HouseInfo>{

    List<HouseInfo> findHouseInfoListByIds(@Param("ids") List<String> ids);

}