package com.hhx.house.service;

import com.hhx.house.HouseApplication;
import com.hhx.house.entity.Community;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;
import java.util.Map;

/**
 * @author hhx
 * @since 2018/2/20 16:32
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HouseApplication.class)
@WebAppConfiguration
public class CommunitySeriviceTest {

    @Autowired
    private CommunityService communitySerivice;

    @Test
    public void getCommunityGroupByDistinct() {
        Map<String, Map<String, List<Community>>> communityGroupByDistinct = communitySerivice
                .getCommunityGroupByDistinct();
        communityGroupByDistinct.get("sh").forEach((k, v) -> System.out.println(k));

    }
}