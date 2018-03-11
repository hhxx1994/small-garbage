package com.hhx.house.service.recommend;

import com.hhx.house.entity.UserTag;
import com.hhx.house.mapping.HouseInfoMapper;
import com.hhx.house.mapping.MapLocationMapper;
import com.hhx.house.mapping.UserTagMapper;
import com.hhx.house.service.HouseInfoService;
import com.hhx.house.vo.SearchVo;
import org.apache.spark.mllib.recommendation.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author hhx
 * @since 2018/2/24 0:02
 */
@Service
public class HouseRecommendService {
    public static final Pattern FLOOR = Pattern.compile("共(\\d+)层");

    public static final Pattern ROOM = Pattern.compile("(\\d+)室(\\d+)厅");

    public static final Pattern SQUARE = Pattern.compile("(\\S+)平米");

    public static final int NUM = 10;
    @Autowired
    private RecommendService recommendService;

    @Autowired
    private UserTagMapper userTagMapper;

    @Autowired
    private MapLocationMapper mapLocationMapper;

    @Autowired
    private HouseInfoMapper houseInfoMapper;

    public List<String> getHouseRecommend(int userId) {
        return getProductIds(recommendService.recommend(userId, NUM));
    }

    public List<String> getRecommendProduct() {
        return getProductIds(recommendService.recommendProductsForUsers(NUM));
    }

    private List<String> getProductIds(Rating[] recommend) {
        return Arrays.stream(recommend)
                .map(Rating::product)
                .map(userTagMapper::selectById)
                .map(UserTag::getHouseId)
                .collect(Collectors.toList());
    }


    @Cacheable(value = "models", key = "#root.methodName")
    public List<SearchVo> getSearchVoList() {
        return houseInfoMapper.selectList(null)
                .stream()
                .map(houseInfo -> {
                    String houseid = houseInfo.getHouseid();
                    String title = houseInfo.getTitle();
                    String community = houseInfo.getCommunity();
                    Matcher matcher = HouseInfoService.COMPILE.matcher(houseInfo.getFloor());
                    int year = 0;
                    if (matcher.find()) {
                        year = Integer.parseInt(matcher.group(1));
                    }
                    Matcher matcher2 = FLOOR.matcher(houseInfo.getFloor());
                    int floor = 0;
                    if (matcher2.find()) {
                        floor = Integer.parseInt(matcher2.group(1));
                    }
                    Matcher matcher3 = ROOM.matcher(houseInfo.getHousetype());
                    int room = 0;
                    int office = 0;
                    if (matcher3.find()) {
                        room = Integer.parseInt(matcher3.group(1));
                        office = Integer.parseInt(matcher3.group(2));
                    }
                    Matcher matcher4 = SQUARE.matcher(houseInfo.getSquare());
                    double square = 0;
                    if (matcher4.find()) {
                        square = Double.parseDouble(matcher4.group(1));
                    }
                    double totalPrice = Double.parseDouble(houseInfo.getTotalprice());
                    double unitPrice = Double.parseDouble(houseInfo.getUnitprice());
                    return SearchVo.builder()
                            .houseId(houseid)
                            .title(title)
                            .community(community)
                            .year(year)
                            .floor(floor)
                            .room(room)
                            .office(office)
                            .square(square)
                            .totalPrice(totalPrice)
                            .unitPrice(unitPrice)
                            .build();

                }).collect(Collectors.toList());

    }


}
