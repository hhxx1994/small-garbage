package com.hhx.house.vo;

import com.hhx.house.entity.HouseInfo;
import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author hhx
 * @since 2018/2/25 9:26
 */
@Data
public class HouseInfoListVo {
    private Integer total;
    private List<HouseVo> list;
}
