package com.happok.hadoop.userstatistics.controller;

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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

        log.info(sql.toString());
        hiveJdbcTemplate.execute(sql.toString());

        return sql.toString();

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

        log.info(sql.toString());
        hiveJdbcTemplate.execute(sql.toString());
        return sql.toString();

        //select substring(time_local,0,2) dt,substring(time_local,13,2) hour from nginx_acc_log where dt='2019-04-15';
        // 03/Apr/2019:15:40:13
        // select t.hour,count(*) cnt
        //         from
        //  (select remote_addr, request_uri,substring(time_local,0,2) dt,substring(time_local,13,2) hour from nginx_acc_log where dt='2019-04-15') t
        //  group by t.hour order by cnt desc ;


        // select t.dt,t.hour,count(t.request_uri) pv,count(distinct t.remote_addr) uv from
        // (select remote_addr, request_uri,substring(time_local,0,2) dt,substring(time_local,13,2) hour from nginx_acc_log where dt='2019-04-15') t
        // group by t.dt,t.hour ;
    }

    @PostMapping("/insert")
    public String insert() {
        hiveJdbcTemplate.execute("insert into hive_test(key, value) values('Neo','Chen')");
        return "Done";
    }

    @GetMapping("/select")
    public String select() {
        String sql = "select * from HIVE_TEST";
        List<Map<String, Object>> rows = hiveJdbcTemplate.queryForList(sql);
        Iterator<Map<String, Object>> it = rows.iterator();
        while (it.hasNext()) {
            Map<String, Object> row = it.next();
            System.out.println(String.format("%s\t%s", row.get("key"), row.get("value")));
        }
        return "Done";
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

        List<NginxInfo> rows = hiveJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(NginxInfo.class));

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
        sql.append("') t ");
        sql.append(" group by t.dt,t.hour");

        List<TrafficEntity> rows = hiveJdbcTemplate.query(sql.toString(), new BeanPropertyRowMapper<>(TrafficEntity.class));
       /* for (TrafficEntity trafficEntity : rows) {
            System.out.println(String.format("%s\t%s\t%s\t%s", trafficEntity.getDt(),
                    trafficEntity.getHour(),
                    trafficEntity.getPv(),
                    trafficEntity.getUv()));
        }*/
        return new ResultBody(rows);
    }

    @DeleteMapping("/delete")
    public String delete() {
        StringBuffer sql = new StringBuffer("DROP TABLE IF EXISTS ");
        sql.append(TABALE_NAME);
        log.info(sql.toString());
        hiveJdbcTemplate.execute(sql.toString());
        return "Done";
    }

}
