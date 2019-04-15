package com.happok.hadoop.userstatistics.result;

/**
 * Created by xiayt on 2018/8/25/025 17:53
 */
public class ErrorInfoInterfaceImpl implements ErrorInfoInterface {

    private String code;
    private String message;

    public ErrorInfoInterfaceImpl(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String getCode() {
        return this.code;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
