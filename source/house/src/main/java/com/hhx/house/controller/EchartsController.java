package com.hhx.house.controller;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hhx.house.constant.AreaConst;
import com.hhx.house.constant.StatisticsConst;
import com.hhx.house.model.Statistics;
import com.hhx.house.service.CommunityService;
import com.hhx.house.service.HouseInfoService;
import com.hhx.house.vo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
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

    @Autowired
    private CommunityService communityService;

    @Cacheable(value = "models", key = "#root.methodName")
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

    @Cacheable(value = "models", key = "#root.methodName")
    @RequestMapping("/houseRatio")
    public Map<String, Integer> ratioData() {
        Map<String, Integer> map = Maps.newHashMap();
        houseInfoService.houseInfoGroupByArea().forEach((k, v) -> map.put(k, v.size()));
        return map;
    }

    @Cacheable(value = "models", key = "#root.methodName")
    @RequestMapping("/userStats")
    public UserStatVo getUserStats() {
        return houseInfoService.getFollowInfo();
    }


    @Cacheable(value = "models", key = "#root.methodName")
    @RequestMapping("/houseAreaRatio")
    public List<HouseAreaRatioVo> getHouseAreaRatio() {
        return houseInfoService.areaPriceRatio();
    }

    @Cacheable(value = "models", key = "#root.methodName")
    @RequestMapping("/subWayData")
    public Map<Boolean, SubWayVo> subWayData() {
        return houseInfoService.subWayData();
    }

    @Cacheable(value = "models", key = "#root.methodName")
    @RequestMapping("/peoplePrice")
    public Map<String, List<PeoplePriceVo>> getPeoplePrice() {
        return houseInfoService.getPeoplePrice();
    }

    @Cacheable(value = "models", key = "#root.methodName")
    @RequestMapping("/yearPrice")
    public Map<String, List<YearPriceVo>> getYearPrice() {
        return houseInfoService.getYearPriceGroupByYear();
    }

    @Cacheable(value = "models", key = "#root.methodName")
    @RequestMapping("/locationData")
    public List<LocationDataVo> getLocationData() {
        return houseInfoService.getLocationData();
    }

    @Cacheable(value = "models", key = "#root.methodName")
    @RequestMapping("/communityByBj")
    public List<CommunityPrice> getCommunityByBj() {
        return communityService.getCommunityByBj();
    }
}
