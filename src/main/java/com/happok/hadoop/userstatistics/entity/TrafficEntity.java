package com.happok.hadoop.userstatistics.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author: xiayt
 * @date: 2019/4/15/015 14:07
 */
@Data
@AllArgsConstructor
public class TrafficEntity implements Serializable {
    private String day;
    private Integer hour;
    private Integer pv;
    private Integer uv;
}
