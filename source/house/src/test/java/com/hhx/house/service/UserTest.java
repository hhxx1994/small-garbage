package com.hhx.house.service;

import com.google.common.collect.Sets;
import com.hhx.house.HouseApplication;
import com.hhx.house.entity.HouseInfo;
import com.hhx.house.entity.User;
import com.hhx.house.entity.UserTag;
import com.hhx.house.mapping.HouseInfoMapper;
import com.hhx.house.mapping.UserMapper;
import com.hhx.house.mapping.UserTagMapper;
import com.hhx.house.service.recommend.RecommendService;
import org.apache.spark.mllib.recommendation.Rating;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Set;


/**
 * @author hhx
 * @since 2018/2/22 14:17
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HouseApplication.class)
@WebAppConfiguration
public class UserTest {

    @Autowired
    HouseInfoMapper houseInfoMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserTagMapper userTagMapper;

    @Autowired
    private RecommendService recommendService;


    @Test
    public void insertUser() {
        User user = new User();
        user.setPassword("123456");
        for (int i = 0; i < 100000; i++) {
            user.setName("黄浩星" + i);
            userMapper.insert(user);
        }
    }

    @Test
    public void insertTag() {
        Random rand = new Random();
        List<HouseInfo> houseInfos = houseInfoMapper.selectList(null);
        userMapper.selectList(null).forEach(user -> {
            Set<String> set = Sets.newHashSet();
            do {
                int i = rand.nextInt(houseInfos.size());
                set.add(houseInfos.get(i).getHouseid());
            } while (set.size() < 5);

            set.forEach(k -> {
                UserTag userTag = new UserTag();
                userTag.setHouseId(k);
                userTag.setUserId(user.getId());
                userTag.setStatus(1);
                userTag.setScore(rand.nextInt(5) + 1);
                userTagMapper.insert(userTag);
            });
        });

    }

    @Test
    public void recommend() {
        long stat = System.currentTimeMillis();
        Rating[] recommend = recommendService.recommend(2, 10);
        long end = System.currentTimeMillis();
        Rating[] recommend1 = recommendService.recommend(3, 10);
        long end2 = System.currentTimeMillis();
        System.out.println("------------------->"+(end-stat));
        System.out.println("------------------->"+(end2-end));
        Arrays.asList(recommend).forEach(System.out::println);
        Arrays.asList(recommend1).forEach(System.out::println);

    }


}
