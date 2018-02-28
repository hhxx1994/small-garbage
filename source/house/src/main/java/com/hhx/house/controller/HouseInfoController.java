package com.hhx.house.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hhx.house.entity.UserTag;
import com.hhx.house.mapping.HouseInfoMapper;
import com.hhx.house.mapping.UserTagMapper;
import com.hhx.house.service.HouseInfoService;
import com.hhx.house.vo.HouseInfoListVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author hhx
 * @since 2018/2/25 9:07
 */
@RestController
@RequestMapping("/houseInfo")
public class HouseInfoController {

    @Autowired
    private HouseInfoMapper houseInfoMapper;

    @Autowired
    private HouseInfoService houseInfoService;

    @Autowired
    private UserTagMapper userTagMapper;

    @RequestMapping("/list")
    public HouseInfoListVo houseInfoList(@RequestParam(defaultValue = "1") Integer currentPage,
                                          Integer userId) {
        return houseInfoService.houseInfoList(currentPage, userId);
    }

    @RequestMapping("/score")
    public void score(float score, String houseId, Integer userId) {
        UserTag userTag = new UserTag();
        userTag.setUserId(userId);
        userTag.setHouseId(houseId);
        EntityWrapper<UserTag> wrapper = new EntityWrapper<>();
        wrapper.setEntity(userTag);
        List<UserTag> userTags = userTagMapper.selectList(wrapper);
        if (userTags == null || !userTags.isEmpty()) {
            userTag.setCount(1);
            userTag.setStatus(3);
            userTag.setScore(score);
            userTagMapper.insert(userTag);
        } else {
            UserTag tag = userTags.get(0);
            tag.setScore(score);
            Integer count = tag.getCount();
            if (count == null) {
                count = 0;
            } else {
                count += 1;
            }
            tag.setCount(count);
            userTagMapper.updateById(tag);
        }

    }
}
