package com.hhx.house.controller;

import com.hhx.house.entity.HouseInfo;
import com.hhx.house.service.HouseInfoService;
import com.hhx.house.vo.HouseInfoListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hhx
 * @since 2018/2/24 0:38
 */
@RestController
@RequestMapping("/recommend")
public class RecommendHouseController {
    @Autowired
    private HouseInfoService houseInfoService;


    @RequestMapping("/houseInfo")
    public List<HouseInfo> getRecommendHouseInfo(int userId) {
        return houseInfoService.getHouseRecommend(userId);
    }

    @RequestMapping("/house")
    public HouseInfoListVo getRecommendHouse(int userId) {
        return houseInfoService.recommendVo(userId);
    }


}
