package com.happok.hadoop.userstatistics.result.feignclient;


import com.alibaba.fastjson.JSONObject;
import com.happok.hadoop.userstatistics.result.ErrorInfoInterface;
import com.happok.hadoop.userstatistics.result.ErrorInfoInterfaceImpl;
import com.happok.hadoop.userstatistics.result.FeignClientException;
import feign.Response;
import feign.Util;
import jdk.nashorn.internal.runtime.regexp.joni.exception.InternalException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;


/**
 * @author xiayt on 2018/9/11/011 11:21
 */
@Configuration
@Slf4j
public class FeignErrorDecoder implements feign.codec.ErrorDecoder {

    @Override
    public Exception decode(String s, Response response) {

        try {
            if (response.body() != null) {
                String body = Util.toString(response.body().asReader());

                JSONObject objBody = JSONObject.parseObject(body);
                if (ObjectUtils.isEmpty(objBody)) {
                    return new InternalException("系统异常,请联系管理员");
                }
                log.error("FeignErrorDecoder:" + objBody.toJSONString());
                ErrorInfoInterface ei = new ErrorInfoInterfaceImpl(objBody.getString("code"), objBody.getString("message"));
                FeignClientException businessException = new FeignClientException(ei);
                return businessException;
            }
        } catch (Exception var4) {
            log.error(var4.getMessage());
            return new InternalException(var4.getMessage());
        }
        return new InternalException("系统异常,请联系管理员");
    }

}
