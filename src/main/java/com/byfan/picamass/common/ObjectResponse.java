package com.byfan.picamass.common;

/**
 * @Author: fby
 * @Description 回传前端的对象
 * @Version 1.0
 * @Date: 2021/11/23 14:24
 */
public class ObjectResponse<T> extends CommonResponse{
    private T result;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public ObjectResponse(T result) {
        this.result = result;
    }

    public ObjectResponse() {
    }

    @Override
    public String toString() {
        return "ObjectResponse{" +
                "result=" + result +
                '}';
    }
}
