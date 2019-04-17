package com.happok.hadoop.userstatistics.controller;

import com.happok.hadoop.userstatistics.entity.AppUseEntity;
import com.happok.hadoop.userstatistics.entity.NginxInfo;
import com.happok.hadoop.userstatistics.entity.TrafficEntity;
import com.happok.hadoop.userstatistics.result.ResultBody;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @author: xiayt
 * @date: 2019/4/15/015 10:35
 */
@Slf4j
@RestController
@RequestMapping("/nginx/access")
public class HiveController {

    final static private String TABALE_NAME = "nginx_acc_log";
    @Autowired
    @Qualifier("hiveDruidTemplate")
    private JdbcTemplate hiveJdbcTemplate;

    @PostMapping("/create")
    public Object create() {

        StringBuffer sql = new StringBuffer("create table IF NOT EXISTS ");
        sql.append(TABALE_NAME);

        sql.append("(remote_addr string," +
                "time_local string," +
                "status string," +
                "body_bytes_sent string," +
                "http_user_agent string," +
                "http_referer string," +
                "request_method string," +
                "request_time string," +
                "request_uri string," +
                "server_protocol string," +
                "request_body string," +
                "http_token string)");

        sql.append("partitioned by (dt string)");
        sql.append("clustered BY (remote_addr)");
        sql.append("INTO 5 buckets stored AS orc TBLPROPERTIES ('transactional' = 'true')");

        log.info("create sql:" + sql.toString());
        hiveJdbcTemplate.execute(sql.toString());

        return new ResultBody();

    }

    @PutMapping("/partition")
    public Object partition() {
        StringBuffer sql = new StringBuffer("alter table ");

        sql.append(TABALE_NAME);
        sql.append(" add if not exists partition");
        sql.append(" ( dt='");

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        sql.append(df.format(new Date()));
        sql.append("')");

        log.info("partition sql:" + sql.toString());
        hiveJdbcTemplate.execute(sql.toString());
        return new ResultBody();

        //select substring(time_local,0,2) dt,substring(time_local,13,2) hour from nginx_acc_log where dt='2019-04-15';
        // 03/Apr/2019:15:40:13
        // select t.hour,count(*) cnt
        //         from
        //  (select remote_addr, request_uri,substring(time_local,0,2) dt,substring(time_local,13,2) hour from nginx_acc_log where dt='2019-04-15') t
        //  group by t.hour order by cnt desc ;


        // select t.dt,t.hour,count(t.request_uri) pv,count(distinct t.remote_addr) uv from
        // (select remote_addr, request_uri,substring(time_local,0,2) dt,substring(time_local,13,2) hour from nginx_acc_log where dt='2019-04-15') t
        // group by t.dt,t.hour ;


        //  select split(request_uri,'/') from  nginx_acc_log where dt='2019-04-15'
        //select t.dt,t.app,t.hour,count(t.app) sum from
        //(select split(request_uri,'/')[2] app ,substring(time_local,9,2) dt,substring(time_local,12,2) hour from  nginx_acc_log where dt='2019-04-17') t
        // group by t.dt,t.hour t.app where t.app is not null;
    }

    @PostMapping("/insert")
    public Object insert() {
        hiveJdbcTemplate.execute("insert into hive_test(key, value) values('Neo','Chen')");
        return new ResultBody();
    }

    @GetMapping("/loginfo")
    public Object getLogInfo(@RequestParam String time) {
        StringBuffer sql = new StringBuffer("select remote_addr" +
                ",time_local " +
                ",status " +
                ",body_bytes_sent " +
                ",http_user_agent " +
                ",http_referer " +
                ",request_method " +
                ",request_time " +
                ",request_uri " +
                ",server_protocol " +
                ",request_body " +
                ",http_token from ");
        sql.append(TABALE_NAME);
        sql.append(" where dt='");
        sql.append(time);
        sql.append("'");
        sql.append(" and remote_addr != '127.0.0.1' order by time_local desc");

        log.info("getLogInfo sql:" + sql.toString());
        List<NginxInfo> rows = hiveJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(NginxInfo.class));

        return new ResultBody(rows);
    }

    @GetMapping("/app")
    public Object getAppUse(@RequestParam String time) {

        StringBuffer sql = new StringBuffer("select t.dt day,t.hour hour ,t.app app ,count(t.app) sum from ");
        sql.append("(select split(request_uri,'/')[1] api,split(request_uri,'/')[2] app ,substring(time_local,9,2) dt,substring(time_local,12,2) hour from ");
        sql.append(TABALE_NAME);
        sql.append(" where dt='");
        sql.append(time);
        sql.append("' and remote_addr != '127.0.0.1' ) t ");
        sql.append(" where (t.api = 'api' " +
                "and t.app != '' " +
                "and t.app != 'disk1' " +
                "and t.app != 'static' " +
                "and t.app != 'images' " +
                "and t.app != 'default') " +
                "group by t.dt,t.hour, t.app");

        log.info("getAppUse sql:" + sql.toString());
        List<AppUseEntity> rows = hiveJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(AppUseEntity.class));
        return new ResultBody(rows);
    }

    @GetMapping("/traffic")
    public Object getTraffic(@RequestParam String time) {

        StringBuffer sql = new StringBuffer("select t.dt day,t.hour hour," +
                "count(t.request_uri) pv,count(distinct t.remote_addr) uv from ");
        sql.append(" (select remote_addr, request_uri,substring(time_local,9,2) dt," +
                "substring(time_local,12,2) hour from ");
        sql.append(TABALE_NAME);
        sql.append(" where dt='");
        sql.append(time);
        sql.append("' and remote_addr != '127.0.0.1' ) t ");
        sql.append(" group by t.dt,t.hour");

        log.info("getTraffic sql:" + sql.toString());
        List<TrafficEntity> rows = hiveJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(TrafficEntity.class));
        return new ResultBody(rows);
    }

    @DeleteMapping("/delete")
    public Object delete() {
        StringBuffer sql = new StringBuffer("DROP TABLE IF EXISTS ");
        sql.append(TABALE_NAME);
        log.info(sql.toString());
        hiveJdbcTemplate.execute(sql.toString());
        return new ResultBody();
    }

}
