package com.happok.hadoop.userstatistics.result;

/**
 * @author xiayt
 * @Valid 无效判断后异常捕获
 */
public class MethodArgumentNotValid implements ErrorInfoInterface {


    private String code;

    private String message;

    MethodArgumentNotValid(String code, String message) {
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

