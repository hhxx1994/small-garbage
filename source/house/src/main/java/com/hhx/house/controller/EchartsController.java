package com.hhx.house.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hhx.house.constant.AreaConst;
import com.hhx.house.constant.StatisticsConst;
import com.hhx.house.model.Statistics;
import com.hhx.house.service.HouseInfoService;
import com.hhx.house.vo.HouseDataVo;
import com.hhx.house.vo.UserStatVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * @author hhx
 * @since 2018/2/16 23:59
 */
@RestController
@RequestMapping("/chartsData")
public class EchartsController {

    @Autowired
    private HouseInfoService houseInfoService;

    @RequestMapping("/houseInfo")
    public HouseDataVo chartsData() {
        List<Double> min = Lists.newArrayList();
        List<Double> max = Lists.newArrayList();
        List<Double> avg = Lists.newArrayList();

        Map<String, Map<String, Statistics>> map = houseInfoService.getHouseInfoStatistics();
        for (int i = 0; i < AreaConst.AREAS.length; i++) {
            Map<String, Statistics> data = map.get(AreaConst.AREAS[i]);
            Statistics statistics = data.get(StatisticsConst.STATISTICS_UNIT);
            min.add(statistics.getMin());
            max.add(statistics.getMax());
            avg.add(statistics.getAvg());
        }

        return HouseDataVo.builder().max(max).min(min).avg(avg).build();
    }

    @RequestMapping("/houseRatio")
    public Map<String, Integer> ratioData() {
        Map<String, Integer> map = Maps.newHashMap();
        houseInfoService.houseInfoGroupByArea().forEach((k, v) -> map.put(k, v.size()));
        return map;
    }

    @RequestMapping("/userStats")
    public UserStatVo getUserStats() {
        return houseInfoService.getFollowInfo();
    }
}
