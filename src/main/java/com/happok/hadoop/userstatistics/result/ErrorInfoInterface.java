package com.happok.hadoop.userstatistics.result;

/**
 * @author xiayt
 * 错误信息格式接口
 */
public interface ErrorInfoInterface {
    String getCode();

    String getMessage();

    void setCode(String code);

    void setMessage(String message);
}
