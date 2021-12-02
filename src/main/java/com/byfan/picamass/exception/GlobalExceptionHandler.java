package com.byfan.picamass.exception;

import com.byfan.picamass.common.CommonResponse;
import com.byfan.picamass.common.ObjectResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author: fby
 * @Description 异常统一处理类
 * @Version 1.0
 * @Date: 2021/11/23 14:49
 */
@RestControllerAdvice       // 捕获全局异常
@Order(1)                   // 类加载顺序
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(PicServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ObjectResponse exception(PicServerException e) {
        ObjectResponse response = new ObjectResponse();
        response.setCode(e.getErrorCode());
        if (StringUtils.isNotEmpty(e.getMessage())) {
            response.setMessage(e.getMessage());
        }
        //打印错误日志，方便跟踪
        StackTraceElement stackTraceElement = e.getStackTrace()[0];
        log.warn("Throw exception location: {}  Exception reason: {}",
                stackTraceElement.toString(), response.getMessage());
        return  response;
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ObjectResponse exception(Exception e) {
        log.error("Exception:", e);
        ObjectResponse response = new ObjectResponse();
        response.setCode(CommonResponse.UNKNOWN_ERROR);
        return  response;
    }
}
