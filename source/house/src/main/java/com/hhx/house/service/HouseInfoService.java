package com.hhx.house.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hhx.house.constant.CacheConst;
import com.hhx.house.constant.StatisticsConst;
import com.hhx.house.entity.HouseInfo;
import com.hhx.house.mapping.HouseInfoMapper;
import com.hhx.house.model.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author hhx
 * @since 2018/2/18 9:47
 */
@Service
public class HouseInfoService {
    private static final Pattern PATTERN = Pattern.compile("https://(\\S+).lianjia.com");
    private Logger logger = LoggerFactory.getLogger(HouseInfoService.class);
    @Autowired
    private HouseInfoMapper houseInfoMapper;

    @Cacheable(value = "models", key = CacheConst.HOUSE_INFO_CACHE)
    public Map<String, List<HouseInfo>> houseInfoGroupByArea() {
        Map<String, List<HouseInfo>> map = Maps.newHashMap();
        houseInfoMapper.selectList(null).forEach(houseInfo -> {
            String link = houseInfo.getLink();
            Matcher matcher = PATTERN.matcher(link);
            if (matcher.find()) {
                String key = matcher.group(1);
                if (map.containsKey(key)) {
                    map.get(key).add(houseInfo);
                } else {
                    ArrayList<HouseInfo> list = Lists.newArrayList();
                    list.add(houseInfo);
                    map.put(key, list);
                }
            }
        });
        logger.info("cache house info");
        return map;
    }

    @Cacheable(value = "models", key = CacheConst.HOUSE_INFO_STATISTICS_CACHE)
    public Map getHouseInfoStatistics() {
        Map<String, List<HouseInfo>> map = houseInfoGroupByArea();
        Map<String, Map<String, Statistics>> data = Maps.newHashMap();
        map.forEach((k, v) -> {
            try {
                Map<String, Statistics> statMap = Maps.newHashMap();
                Statistics unit = getStatistics(v, HouseInfo::getUnitprice);
                Statistics total = getStatistics(v, HouseInfo::getTotalprice);

                statMap.put(StatisticsConst.STATISTICS_UNIT, unit);
                statMap.put(StatisticsConst.STATISTICS_TOTAL, total);
                data.put(k, statMap);
            } catch (Exception e) {
                logger.info(e.getMessage());
            }
        });
        return data;
    }

    private Statistics getStatistics(List<HouseInfo> v, Function<HouseInfo, String> function) {
        Double min = v.stream()
                .map(function)
                .map(Double::parseDouble)
                .min(Comparator.naturalOrder())
                .orElse(0d);
        Double max = v.stream()
                .map(function)
                .map(Double::parseDouble)
                .max(Comparator.naturalOrder())
                .orElse(0d);
        Double avg = v.stream().map(function)
                .map(Double::parseDouble)
                .collect(Collectors.averagingDouble(item -> item));
        return new Statistics(min, max, avg);
    }
}
