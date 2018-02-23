package com.hhx.house.controller;

import com.google.common.collect.Lists;
import com.hhx.house.entity.User;
import com.hhx.house.entity.UserTag;
import com.hhx.house.mapping.UserMapper;
import com.hhx.house.mapping.UserTagMapper;
import com.hhx.house.service.UserTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * @author hhx
 * @since 2018/2/22 23:57
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserTagService userTagService;

    @Autowired
    private UserTagMapper userTagMapper;


    @RequestMapping("/regin")
    public User regin(String username, String password) {
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        Integer status = userMapper.insert(user);
        if (status == 1) {
            return user;
        }
        return null;
    }

    @RequestMapping("label")
    public List<String> getLabel() {
        return userTagService.getTags();
    }

    @RequestMapping("tags")
    public void getTags(String[] tags, Integer userId) {
        List<UserTag> userTags = Lists.newArrayList();
        userTagService.getList().forEach(wordTuple -> {
            Arrays.asList(tags).forEach(tag -> {
                if (Objects.equals(wordTuple.getKey(), tag)) {
                    wordTuple.getHouseId().forEach(id -> {
                        UserTag userTag = new UserTag();
                        userTag.setScore(4.5f);
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
