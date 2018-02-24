package com.hhx.house.controller;

import com.hhx.house.async.AsyncTask;
import com.hhx.house.entity.User;
import com.hhx.house.mapping.UserMapper;
import com.hhx.house.mapping.UserTagMapper;
import com.hhx.house.service.UserTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @author hhx
 * @since 2018/2/22 23:57
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private AsyncTask asyncTask;

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
        user.setRegisterDate(new Date());
        userMapper.insert(user);
        return userMapper.selectOne(user);
    }

    @RequestMapping("label")
    public List<String> getLabel() {
        return userTagService.getTags();
    }

    @RequestMapping("tags")
    public void getTags(String[] tags, Integer userId) {
        asyncTask.batchInsertUserTags(tags, userId);
    }
    @RequestMapping("login")
    public User login(String username, String password) {
        User user = new User();
        user.setName(username);
        user.setPassword(password);
        return userMapper.selectOne(user);
    }


}
