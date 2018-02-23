package com.hhx.house.service.recommend;

import com.hhx.house.entity.UserTag;
import com.hhx.house.mapping.UserTagMapper;
import org.apache.spark.mllib.recommendation.Rating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author hhx
 * @since 2018/2/24 0:02
 */
@Service
public class HouseRecommendService {
    @Autowired
    private RecommendService recommendService;

    @Autowired
    private UserTagMapper userTagMapper;
    public List<String> getHouseRecommend(int userId) {
        return Arrays.stream(recommendService.recommend(userId, 10))
                .map(Rating::product)
                .map(userTagMapper::selectById)
                .map(UserTag::getHouseId)
                .collect(Collectors.toList());
    }
}
