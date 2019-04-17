package com.happok.hadoop.userstatistics.entity;

import lombok.Data;

/**
 * @author: xiayt
 * @date: 2019/4/17/017 15:04
 */
@Data
public class AppUseEntity {
    private String day;
    private Integer hour;
    private String app;
    private Integer sum;
}
