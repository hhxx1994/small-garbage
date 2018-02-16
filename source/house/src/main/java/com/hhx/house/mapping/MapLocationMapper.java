package com.hhx.house.mapping;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hhx.house.entity.MapLocation;

import java.util.List;

public interface MapLocationMapper extends BaseMapper<MapLocation> {

    List<MapLocation> selectAll();
}