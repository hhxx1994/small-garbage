package com.hhx.house.async;

import com.google.common.collect.Lists;
import com.hhx.house.entity.UserTag;
import com.hhx.house.mapping.UserMapper;
import com.hhx.house.mapping.UserTagMapper;
import com.hhx.house.service.UserTagService;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Component
public class AsyncTask {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserTagService userTagService;

    @Autowired
    private UserTagMapper userTagMapper;

    @Async
    public void batchInsertUserTags(String[] tags, Integer userId) {

        List<UserTag> userTags = Lists.newArrayList();
        userTagService.getList().forEach(wordTuple -> {
            Arrays.asList(tags).forEach(tag -> {
                if (Objects.equals(wordTuple.getKey(), tag)) {
                    wordTuple.getHouseId().forEach(id -> {
                        UserTag userTag = new UserTag();
                        userTag.setScore(RandomUtils.nextFloat(2.5f,4f));
                        userTag.setUserId(userId);
                        userTag.setHouseId(id);
                        userTags.add(userTag);
                    });
                }
            });
        });
        userTagMapper.insertBatch(userTags);
    }
}  
