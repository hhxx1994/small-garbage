package com.hhx.house.vo;

import lombok.Builder;
import lombok.Data;

/**
 * @author hhx
 * @since 2018/2/18 20:11
 */
@Data
@Builder
public class UserStatVo {

    private long focus;
    private long watch;
    private long sell;
    private long rent;
}
