package com.hhx.house;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hhx.house.entity.Gps;
import com.hhx.house.entity.MapLocation;
import com.hhx.house.mapping.MapLocationMapper;
import com.hhx.house.service.recommend.HouseRecommendService;
import com.hhx.house.utils.PositionUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = HouseApplication.class)
@WebAppConfiguration
public class HouseApplicationTests {

    @Autowired
    private MapLocationMapper mapLocationMapper;

    @Autowired
    private HouseRecommendService houseRecommendService;

    @Test
    public void contextLoads() {

    }

    @Test
    public void translationPosition() {
        Wrapper<MapLocation> wrapper = new EntityWrapper<MapLocation>().where("status=1").and("gcj_lng is null");
        mapLocationMapper.selectList(wrapper).forEach(entity -> {
            String lat = entity.getLat();
            String lng = entity.getLng();
            Gps gps = PositionUtil.bd09_To_Gcj02(Double.parseDouble(lat), Double.parseDouble(lng));
            entity.setGcjLat(Double.toString(gps.getWgLat()));
            entity.setGcjLng(Double.toString(gps.getWgLon()));
            mapLocationMapper.updateById(entity);
        });

    }

    @Test
    public void printSql() {
        Wrapper<MapLocation> wrapper = new EntityWrapper<>();
        int size = mapLocationMapper.selectList(wrapper).size();
        System.out.println("size=" + size);

    }

    @Test
    public void houseRecommendServiceTest() {
        houseRecommendService.getSearchVoList().forEach(System.out::println);
    }

}
