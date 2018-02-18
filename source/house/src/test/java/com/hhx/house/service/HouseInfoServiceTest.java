package com.hhx.house.service;

import com.hhx.house.HouseApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author hhx
 * @since 2018/2/18 10:07
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HouseApplication.class)
@WebAppConfiguration
public class HouseInfoServiceTest {

    @Autowired
    private HouseInfoService houseInfoService;

    @Test
    public void houseInfoGroupByArea() {
        int size = houseInfoService.houseInfoGroupByArea().size();
        System.out.println(size);

    }
}