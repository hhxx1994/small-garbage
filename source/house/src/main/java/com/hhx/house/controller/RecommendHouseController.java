package com.hhx.house.controller;

import com.hhx.house.entity.HouseInfo;
import com.hhx.house.mapping.MapLocationMapper;
import com.hhx.house.model.SearchParam;
import com.hhx.house.service.HouseInfoService;
import com.hhx.house.service.recommend.HouseRecommendService;
import com.hhx.house.vo.HouseInfoListVo;
import com.hhx.house.vo.LocationDataVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author hhx
 * @since 2018/2/24 0:38
 */
@RestController
@RequestMapping("/recommend")
public class RecommendHouseController {
    @Autowired
    private HouseInfoService houseInfoService;

    @Autowired
    private HouseRecommendService houseRecommendService;

    @Autowired
    private MapLocationMapper mapLocationMapper;


    @RequestMapping("/houseInfo")
    public List<HouseInfo> getRecommendHouseInfo(int userId) {
        return houseInfoService.getHouseRecommend(userId);
    }

    @RequestMapping("/house")
    public HouseInfoListVo getRecommendHouse(int userId) {
        return houseInfoService.recommendVo(userId);
    }

    @RequestMapping("/search")
    public List<LocationDataVo> search(@RequestBody SearchParam searchParam) {
        return houseRecommendService.getSearchVoList()
                .stream()
                .filter(searchVo -> {
                    return searchParam.getUnitPrice().get(0) <= searchVo.getUnitPrice() &&
                            searchVo.getUnitPrice() <= searchParam.getUnitPrice().get(1);
                })
                .filter(searchVo -> {
                    return searchParam.getArea().get(0) <= searchVo.getSquare() &&
                            searchVo.getSquare() <= searchParam.getArea().get(1);
                })
                .filter(searchVo -> {
                    return searchParam.getYear().get(0) <= searchVo.getYear() &&
                            searchVo.getYear() <= searchParam.getYear().get(1);
                })
                .filter(searchVo -> {
                    if (StringUtils.isBlank(searchParam.getName())) {
                        return true;
                    } else {
                        return Objects.equals(searchVo.getCommunity(), searchParam.getName());
                    }
                }).map(searchVo -> {
                    return LocationDataVo.builder()
                            .name(searchVo.getCommunity())
                            .value(searchVo.getUnitPrice())
                            .coords(Arrays.asList(searchVo.getLat(), searchVo.getLng()))
                            .build();

                }).collect(Collectors.toList());

    }


}
