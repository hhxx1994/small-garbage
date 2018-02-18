package com.hhx.house.service;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hhx.house.constant.AreaConst;
import com.hhx.house.constant.StatisticsConst;
import com.hhx.house.entity.HouseInfo;
import com.hhx.house.mapping.CommunityMapper;
import com.hhx.house.mapping.HouseInfoMapper;
import com.hhx.house.mapping.RentInfoMapper;
import com.hhx.house.mapping.SellInfoMapper;
import com.hhx.house.model.Statistics;
import com.hhx.house.vo.HouseAreaRatioVo;
import com.hhx.house.vo.SubWayVo;
import com.hhx.house.vo.UserStatVo;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

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

    public static final String DOMAIN = ".lianjia.com";
    public static final Pattern PATTERN = Pattern.compile("(\\d+)\\D+(\\d+)");
    private Logger logger = LoggerFactory.getLogger(HouseInfoService.class);
    @Autowired
    private HouseInfoMapper houseInfoMapper;

    @Autowired
    private RentInfoMapper rentInfoMapper;

    @Autowired
    private SellInfoMapper sellInfoMapper;

    @Autowired
    private CommunityMapper communityMapper;

    @Cacheable(value = "models", key = "#root.methodName")
    public Map<String, List<HouseInfo>> houseInfoGroupByArea() {
        Map<String, List<HouseInfo>> map = Maps.newHashMap();
        for (int i = 0; i < AreaConst.AREAS.length; i++) {
            map.put(AreaConst.AREAS[i], Lists.newArrayList());
        }
        houseInfoMapper.selectList(null).forEach(houseInfo -> {
            String link = houseInfo.getLink();
            for (int i = 0; i < AreaConst.AREAS.length; i++) {
                if (link.contains(AreaConst.AREAS[i] + DOMAIN)) {
                    map.get(AreaConst.AREAS[i]).add(houseInfo);
                    return;
                }
            }
        });
        logger.info("cache house info");
        return map;
    }

    @Cacheable(value = "models", key = "#root.methodName")
    public Map<String, Map<String, Statistics>> getHouseInfoStatistics() {
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

    public UserStatVo getFollowInfo() {
        List<HouseInfo> houseInfos = houseInfoMapper.selectList(null);
        long focus = houseInfos.stream().map(HouseInfo::getFollowinfo).map(info -> {
            Matcher matcher = PATTERN.matcher(info);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(1));
            }
            return 0;
        }).mapToInt(item -> item).sum();

        long watch = houseInfos.stream().map(HouseInfo::getFollowinfo).map(info -> {
            Matcher matcher = PATTERN.matcher(info);
            if (matcher.find()) {
                return Integer.parseInt(matcher.group(2));
            }
            return 0;
        }).mapToInt(item -> item).sum();


        return UserStatVo.builder().focus(focus).watch(watch).rent(rentInfoMapper.selectCount(null)).sell
                (sellInfoMapper.selectCount(null)).build();

    }

    public List<HouseAreaRatioVo> areaPriceRatio() {
        return houseInfoMapper.selectList(null).stream().map(houseInfo -> {
            String square = houseInfo.getSquare();
            try {
                double area = Double.parseDouble(square.substring(0, square.length() - 2));
                double price = Double.parseDouble(houseInfo.getUnitprice());
                return new HouseAreaRatioVo(area, price);
            } catch (Exception e) {
                return new HouseAreaRatioVo(0, 0);
            }
        }).collect(Collectors.toList());
    }

    public Map<Boolean, SubWayVo> subWayData() {
        Map<Boolean, SubWayVo> map = Maps.newHashMap();
        communityMapper.selectList(null).stream()
                .collect(Collectors.groupingBy(community -> StringUtils.isNotBlank(community.getTaglist())))
                .forEach((k, v) -> {
                    List<Double> collect = v.stream().map(ele -> Double.parseDouble(ele.getPrice()))
                            .sorted(Comparator.naturalOrder()).collect(Collectors.toList());
                    int size = collect.size();
                    SubWayVo subWayVo = SubWayVo.builder().lower(collect.get(0))
                            .upper(collect.get(size - 1))
                            .median(collect.get(size / 2))
                            .q1(collect.get(new Double(size * 0.25).intValue()))
                            .q3(collect.get(new Double(size * 0.75).intValue()))
                            .build();
                    map.put(k, subWayVo);
                });
        return map;

    }
}
