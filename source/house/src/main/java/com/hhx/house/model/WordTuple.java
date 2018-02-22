package com.hhx.house.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * @author hhx
 * @since 2018/2/22 11:39
 */
@Data
@Builder
@AllArgsConstructor
public class WordTuple {
    private String key;
    private Integer point;
}
