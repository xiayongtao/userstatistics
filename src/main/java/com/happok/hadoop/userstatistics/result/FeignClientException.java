package com.happok.hadoop.userstatistics.result;

/**
 * @author xiayt
 * Feign错误异常
 */
public class FeignClientException extends Exception {

    private ErrorInfoInterface errorInfo;

    public FeignClientException(ErrorInfoInterface errorInfo) {
        this.errorInfo = errorInfo;
    }

    public ErrorInfoInterface getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ErrorInfoInterface errorInfo) {
        this.errorInfo = errorInfo;
    }
}