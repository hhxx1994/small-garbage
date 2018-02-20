package com.hhx.house.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hhx.house.constant.AreaConst;
import com.hhx.house.entity.Community;
import com.hhx.house.mapping.*;
import com.hhx.house.vo.CommunityPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author hhx
 * @since 2018/2/20 16:12
 */
@Service
public class CommunityService {
    @Autowired
    private HouseInfoMapper houseInfoMapper;
    @Autowired
    private RentInfoMapper rentInfoMapper;
    @Autowired
    private SellInfoMapper sellInfoMapper;
    @Autowired
    private CommunityMapper communityMapper;
    @Autowired
    private MapLocationMapper mapLocationMapper;

    public Map<String, List<Community>> getCommunityGroupByArea() {
        return communityMapper.selectList(null).stream().collect(Collectors.groupingBy(community -> {
            String link = community.getLink();
            for (int i = 0; i < AreaConst.AREAS.length; i++) {
                if (link.contains(AreaConst.AREAS[i] + HouseInfoService.DOMAIN)) {
                    return AreaConst.AREAS[i];
                }
            }
            return link;
        }));
    }

    public Map<String, Map<String, List<Community>>> getCommunityGroupByDistinct() {
        Map<String, Map<String, List<Community>>> map = Maps.newHashMap();
        getCommunityGroupByArea().forEach((k, v) -> {
            Map<String, List<Community>> collect = v.stream().collect(Collectors.groupingBy(Community::getDistrict));
            map.put(k, collect);
        });
        return map;
    }

    public List<CommunityPrice> getCommunityByBj() {
        List<CommunityPrice> list = Lists.newArrayList();
        getCommunityGroupByDistinct().get("bj").forEach((k, v) -> {
            String key = k;
            if (!k.contains("区")) {
                key = k + "区";
            }
            Double avg = v.stream().map(Community::getPrice).map(Double::parseDouble).collect(Collectors.averagingDouble(item -> item));
            list.add(CommunityPrice.builder().name(key).price(avg).build());
        });
        return list;
    }


}
