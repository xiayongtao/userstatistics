package com.happok.hadoop.userstatistics.result;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.RestClientException;

import java.util.List;

/**
 * @author xiayo
 */
@RestControllerAdvice
public class GlobalErrorInfoHandler {

    @ExceptionHandler(value = GlobalErrorInfoException.class)
    public ResultBody errorHandlerOverJson(GlobalErrorInfoException exception) {
        ErrorInfoInterface errorInfo = exception.getErrorInfo();
        ResultBody result = new ResultBody(errorInfo);
        return result;
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResultBody duplicateleHandler(DuplicateException e) {
        ErrorInfoInterface errorInfo = e.getErrorInfo();
        ResultBody result = new ResultBody(errorInfo);
        return result;
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ResultBody duplicateKeyeHandler(DuplicateKeyException e) {
        ErrorInfoInterface errorInfo = new ErrorInfoInterfaceImpl(HttpStatus.TOO_MANY_REQUESTS.toString(), e.getMessage());
        ResultBody result = new ResultBody(errorInfo);
        return result;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResultBody handleException(MethodArgumentNotValidException exception) {

        BindingResult bindingResult = exception.getBindingResult();
        final List<ObjectError> errorList = bindingResult.getAllErrors();
        String error = "[";
        for (ObjectError objectError : errorList) {
            error += objectError.getDefaultMessage() + "|";
        }
        error = error.substring(0, error.lastIndexOf("|"));
        error += "]";

        ErrorInfoInterface errorInfo = new MethodArgumentNotValid(HttpStatus.BAD_REQUEST.toString(), error);
        ResultBody result = new ResultBody(errorInfo);
        return result;
    }

    @ExceptionHandler(FeignClientException.class)
    @ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
    public ResultBody FeignException(FeignClientException exception) {
        ResultBody result = new ResultBody(exception.getErrorInfo());
        return result;
    }

    @ExceptionHandler(RestClientException.class)
    @ResponseStatus(value = HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS)
    public ResultBody RestClientException(RestClientException exception) {

        ErrorInfoInterface errorInfo = new ErrorInfoInterfaceImpl(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS.toString(), exception.getMessage());
        ResultBody result = new ResultBody(errorInfo);
        return result;
    }
}
