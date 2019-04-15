package com.happok.hadoop.userstatistics.result;

/**
 * @author xiayt
 * 重复信息错误异常
 */
public class DuplicateException extends Exception {

    private ErrorInfoInterface errorInfo;

    public DuplicateException(ErrorInfoInterface errorInfo) {
        this.errorInfo = errorInfo;
    }

    public ErrorInfoInterface getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(ErrorInfoInterface errorInfo) {
        this.errorInfo = errorInfo;
    }
}