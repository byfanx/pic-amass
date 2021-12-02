package com.byfan.picamass.exception;

import com.byfan.picamass.common.CommonResponse;

/**
 * @Author: fby
 * @Description 服务端抛出异常
 * @Version 1.0
 * @Date: 2021/11/23 14:57
 */
public class PicServerException extends Exception{

    private Integer errorCode;

    public PicServerException(Integer errorCode) {
        super(CommonResponse.STATUS_DEFAULT_MESSAGE.get(errorCode));
        this.errorCode = errorCode;
    }

    public PicServerException(String message) {
        super(message);
    }

    public PicServerException(String message, Integer errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }
}
