package com.happok.hadoop.userstatistics.entity;

import lombok.Data;

/**
 * @author: xiayt
 * @date: 2019/4/15/015 14:07
 */
@Data
public class TrafficEntity {
    private String dt;
    private Integer hour;
    private Integer pv;
    private Integer uv;
}
