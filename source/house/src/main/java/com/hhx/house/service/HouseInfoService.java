package com.hhx.house.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hhx.house.constant.AreaConst;
import com.hhx.house.constant.StatisticsConst;
import com.hhx.house.entity.*;
import com.hhx.house.mapping.*;
import com.hhx.house.model.Statistics;
import com.hhx.house.service.recommend.HouseRecommendService;
import com.hhx.house.utils.PositionUtil;
import com.hhx.house.vo.*;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
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
    public static final Pattern COMPILE = Pattern.compile("(\\d+)年");
    private Logger logger = LoggerFactory.getLogger(HouseInfoService.class);
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
    @Autowired
    private HouseImgMapper houseImgMapper;

    @Autowired
    private UserTagMapper userTagMapper;


    @Autowired
    private UserMapper userMapper;

    @Autowired
    private HouseRecommendService houseRecommendService;

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


    public Map<String, List<PeoplePriceVo>> getPeoplePrice() {
        Map<String, List<PeoplePriceVo>> map = Maps.newHashMap();
        for (int i = 0; i < AreaConst.AREAS.length; i++) {
            map.put(AreaConst.AREAS[i], Lists.newArrayList());
        }
        communityMapper.selectList(null).stream().collect(Collectors.groupingBy(community -> {
            String link = community.getLink();
            for (int i = 0; i < AreaConst.AREAS.length; i++) {
                if (link.contains(AreaConst.AREAS[i] + DOMAIN)) {
                    return AreaConst.AREAS[i];
                }
            }
            return link;
        })).forEach((k, v) -> {
            v.forEach(community -> {
                String num = community.getHouseNum();
                int houseNum = Integer.parseInt(num.substring(0, num.length() - 1));
                double price = Double.parseDouble(community.getPrice());
                if (map.containsKey(k)) {
                    map.get(k).add(new PeoplePriceVo(houseNum, price));
                }
            });
        });
        return map;
    }

    public Map<String, List<YearPriceVo>> getYearPrice() {
        Map<String, List<YearPriceVo>> map = Maps.newHashMap();
        for (int i = 0; i < AreaConst.AREAS.length; i++) {
            map.put(AreaConst.AREAS[i], Lists.newArrayList());
        }
        houseInfoGroupByArea().forEach((k, v) -> {
            v.stream().filter(houseInfo -> {
                String floor = houseInfo.getFloor();
                int year = 0;
                Matcher matcher = COMPILE.matcher(floor);
                if (matcher.find()) {
                    year = Integer.parseInt(matcher.group(1));
                }
                return year >= 2000;
            }).forEach(houseInfo -> {
                String floor = houseInfo.getFloor();
                int year = 0;
                double total = Double.parseDouble(houseInfo.getUnitprice());
                Matcher matcher = COMPILE.matcher(floor);
                if (matcher.find()) {
                    year = Integer.parseInt(matcher.group(1));
                }
                if (map.containsKey(k)) {
                    map.get(k).add(new YearPriceVo(year, total));
                }
            });
        });
        return map;
    }

    public Map<String, List<YearPriceVo>> getYearPriceGroupByYear() {
        Map<String, List<YearPriceVo>> map = Maps.newHashMap();
        for (int i = 0; i < AreaConst.AREAS.length; i++) {
            map.put(AreaConst.AREAS[i], Lists.newArrayList());
        }
        getYearPrice().forEach((k, v) -> {
            Map<Integer, List<YearPriceVo>> collect = v.stream().collect(Collectors.groupingBy(YearPriceVo::getYear));
            collect.forEach((k2, v2) -> {
                Double total = v2.stream().map(YearPriceVo::getPrice).collect(Collectors.averagingDouble(item -> item));
                if (map.containsKey(k)) {
                    map.get(k).add(new YearPriceVo(k2, total));
                }
            });
        });
        return map;

    }

    public List<LocationDataVo> getLocationData() {
        return mapLocationMapper.getLocationData().stream().map(mapLocation -> {
            String community = mapLocation.getCommunity();
            double price = mapLocation.getPrice();
            double gcjLat = mapLocation.getGcjLat();
            double gcjLng = mapLocation.getGcjLng();
            Gps gps = PositionUtil.bd09_To_Gps84(gcjLat, gcjLng);
            return new LocationDataVo(community.trim(), price, Arrays.asList(gps.getWgLon(), gps.getWgLat()));
        })
                //bj,gz,sh,sz
                .filter(getLocationDataVoPredicate(0, 117.30d, 115.25d, 114.3d, 112.57d, 122.2d, 120.85d, 114.37d,
                        113.52d))
                .filter(getLocationDataVoPredicate(1, 41.03d, 39.26d, 23.56d, 22.26d, 31.8833d, 30.6667d,
                        22.52d, 22.27d))
                .collect(Collectors.toList());

    }

    private Predicate<LocationDataVo> getLocationDataVoPredicate(int i, double v, double v2, double v3, double v4,
                                                                 double v5, double v6, double v7, double v8) {
        return locationDataVo ->
                (locationDataVo.getCoords().get(i) < v && locationDataVo.getCoords().get(i) > v2) ||
                        (locationDataVo.getCoords().get(i) < v3 && locationDataVo.getCoords().get(i) > v4) ||
                        (locationDataVo.getCoords().get(i) < v5 && locationDataVo.getCoords().get(i) > v6) ||
                        (locationDataVo.getCoords().get(i) < v7 && locationDataVo.getCoords().get(i) > v8);


    }

    public List<LocationDataVo> getLocationData2(double lngMin, double lngMax, double latMin, double latMax) {
        return mapLocationMapper.getLocationData().stream().map(mapLocation -> {
            String community = mapLocation.getCommunity();
            double price = mapLocation.getPrice();
            double gcjLat = mapLocation.getGcjLat();
            double gcjLng = mapLocation.getGcjLng();
            Gps gps = PositionUtil.bd09_To_Gps84(gcjLat, gcjLng);
            return new LocationDataVo(community.trim(), price, Arrays.asList(gps.getWgLon(), gps.getWgLat()));
        })
                .filter(locationDataVo -> locationDataVo.getCoords().get(0) > lngMin && locationDataVo.getCoords().get(0) < lngMax)
                .filter(locationDataVo -> locationDataVo.getCoords().get(1) > latMin && locationDataVo.getCoords().get(1) < latMax)
                .collect(Collectors.toList());

    }

    public List<HouseInfo> getHouseRecommend(int userId) {
        User user = userMapper.selectById(userId);
        List<String> houseIds;
        if (Objects.equals(user.getStatus(), 1)) {
            houseIds = houseRecommendService.getHouseRecommend(userId);
        } else {
            houseIds = houseRecommendService.getRecommendProduct();
        }

        return houseInfoMapper.findHouseInfoListByIds(houseIds);
    }

    @Cacheable(value = "models", key = "#root.methodName")
    public List<String> houseImg() {
        return houseImgMapper.selectList(null).stream()
                .map(HouseImg::getImg)
                .map(img -> {
                    String[] split = img.split("/");
                    return split[split.length - 1];
                }).collect(Collectors.toList());
    }

    public HouseInfoListVo houseInfoList(Integer currentPage, Integer userId) {
        Page<HouseInfo> page = new Page<>(currentPage, 10);
        List<HouseInfo> houseInfos = houseInfoMapper.houseInfoList(page);
        HouseInfoListVo houseInfoListVo = getHouseInfoListVo(userId, houseInfos);
        houseInfoListVo.setTotal(houseInfoMapper.selectCount(null));
        return houseInfoListVo;
    }

    public HouseInfoListVo recommendVo(int userId) {
        List<HouseInfo> houseRecommend = getHouseRecommend(userId);
        return getHouseInfoListVo(userId, houseRecommend);
    }

    private HouseInfoListVo getHouseInfoListVo(int userId, List<HouseInfo> houseRecommend) {
        HouseInfoListVo houseInfoListVo = new HouseInfoListVo();
        List<HouseVo> house = new ArrayList<>();
        List<String> imgs = houseImg();
        houseRecommend.forEach(houseInfo -> {
            HouseVo houseVo = new HouseVo();
            houseVo.setLink(houseInfo.getLink());
            houseVo.setTitle(houseInfo.getTitle());
            houseVo.setHouseId(houseInfo.getHouseid());
            int index = RandomUtils.nextInt(0, imgs.size());
            houseVo.setImg("/static/img/" + imgs.get(index));
            UserTag userTag = new UserTag();
            userTag.setUserId(userId);
            userTag.setHouseId(houseInfo.getHouseid());
            userTag.setStatus(3);

            EntityWrapper<UserTag> wrapper = new EntityWrapper<>();
            wrapper.setEntity(userTag);
            List<UserTag> userTags = userTagMapper.selectList(wrapper);
            Float score = 0f;
            if (userTags != null && !userTags.isEmpty()) {
                score = Optional.ofNullable(userTags.get(0).getScore()).orElse(0f);
            }
            houseVo.setScore(score);
            house.add(houseVo);
        });
        houseInfoListVo.setList(house);
        return houseInfoListVo;
    }


}
