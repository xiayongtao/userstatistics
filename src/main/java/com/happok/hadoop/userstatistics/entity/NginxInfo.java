package com.happok.hadoop.userstatistics.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @author: xiayt
 * @date: 2019/4/15/015 14:27
 */
@Data
public class NginxInfo implements Serializable {
    private String remote_addr;
    private String time_local;
    private String status;
    private String body_bytes_sent;
    private String http_user_agent;
    private String http_referer;
    private String request_method;
    private String request_time;
    private String request_uri;
    private String server_protocol;
    private String request_body;
    private String http_token;
}
