package com.hhx.house.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.hhx.house.entity.HouseInfo;
import com.hhx.house.entity.UserTag;
import com.hhx.house.mapping.HouseInfoMapper;
import com.hhx.house.mapping.UserTagMapper;
import com.hhx.house.service.HouseInfoService;
import com.hhx.house.vo.HouseInfoListVo;
import com.hhx.house.vo.HouseVo;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
                                         @RequestParam(defaultValue = "10007") Integer userId) {

        Page<HouseInfo> page = new Page<>(currentPage, 10);
        List<String> imgs = houseInfoService.houseImg();
        HouseInfoListVo houseInfoListVo = new HouseInfoListVo();
        houseInfoListVo.setTotal(houseInfoMapper.selectCount(null));
        List<HouseVo> house = new ArrayList<>();
        houseInfoMapper.houseInfoList(page).forEach(houseInfo -> {
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
            Float score = Optional.ofNullable(userTagMapper.selectOne(userTag)).map(t -> Optional.ofNullable(t.getScore()).orElse
                    (0f)).orElse(0f);
            houseVo.setScore(score);
            house.add(houseVo);
        });
        houseInfoListVo.setList(house);
        return houseInfoListVo;
    }

    @RequestMapping("/score")
    public void score(float score, String houseId, @RequestParam(defaultValue = "10007") Integer userId) {
        UserTag userTag = new UserTag();
        userTag.setUserId(userId);
        userTag.setHouseId(houseId);
        UserTag tag = userTagMapper.selectOne(userTag);
        if (tag == null) {
            userTag.setCount(1);
            userTag.setStatus(3);
            userTag.setScore(score);
            userTagMapper.insert(userTag);
        } else {
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
