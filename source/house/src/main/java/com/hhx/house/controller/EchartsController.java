package com.hhx.house.controller;

import com.hhx.house.entity.Hisprice;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author hhx
 * @since 2018/2/16 23:59
 */
@RestController
public class EchartsController {
    @RequestMapping("/chartsData")
    public Hisprice chartsData() {
        Hisprice hisprice = new Hisprice();
        hisprice.setDate(new Date());
        hisprice.setHouseid("1234");
        hisprice.setTotalprice("2345");
        return hisprice;
    }
}
