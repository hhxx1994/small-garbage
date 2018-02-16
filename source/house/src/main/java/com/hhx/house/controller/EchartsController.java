package com.hhx.house.controller;

import com.hhx.house.vo.HouseDataVo;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author hhx
 * @since 2018/2/16 23:59
 */
@RestController
public class EchartsController {
    @RequestMapping("/chartsData")
    public HouseDataVo chartsData() {
        return HouseDataVo.builder().max(Arrays.asList(100d, 200d, 300d))
                .min(Arrays.asList(20d, 30d, 80d))
                .avg(Arrays.asList(50d, 60d, 70d)).build();

    }
}
