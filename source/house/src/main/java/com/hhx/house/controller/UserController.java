package com.hhx.house.controller;

import com.hhx.house.entity.User;
import com.hhx.house.mapping.UserMapper;
import com.hhx.house.model.WordTuple;
import com.hhx.house.service.UserTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

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


    @RequestMapping("/regin")
    public User regin(String username, String password) {
        System.out.println("username="+username);
        System.out.println("password="+password);
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
    public List<String> getLabel(){
       return userTagService.getList().stream().map(WordTuple::getKey).collect(Collectors.toList());
    }
}
