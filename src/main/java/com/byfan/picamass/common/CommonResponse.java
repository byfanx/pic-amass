package com.byfan.picamass.common;


import java.util.HashMap;
import java.util.Map;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/23 14:21
 */
public class CommonResponse {
    public static final Integer STATUS_OK = 200;
    // 10X 其它
    public static final Integer STATUS_JSON_SYNTAX_ERROR = 1000;
    // 1001 未知错误
    public static final Integer UNKNOWN_ERROR = 1001;
    // 1002 参数错误
    public static final Integer PARAM_ERROR = 1002;
    // 1003 资源不存在
    public static final Integer RESOURCE_NOT_EXIST = 1003;
    // 1004 资源名称已存在
    public static final Integer REPEATED_RESOURCE_NAME = 1004;
    // 1005 删除资源失败
    public static final Integer DEL_RESOURCE_FAIl = 1005;
    // 1006 文件格式不支持
    public static final Integer FILE_FORMAT_ERROR = 1006;
    // 1007 文件超出限制
    public static final Integer FILE_TOO_LARGE = 1007;

    public static final Map<Integer, String> STATUS_DEFAULT_MESSAGE = new HashMap<Integer, String>();

    static {
        STATUS_DEFAULT_MESSAGE.put(STATUS_OK,"Success");
        STATUS_DEFAULT_MESSAGE.put(STATUS_JSON_SYNTAX_ERROR,"Json解析异常");
        STATUS_DEFAULT_MESSAGE.put(UNKNOWN_ERROR,"未知错误");
        STATUS_DEFAULT_MESSAGE.put(PARAM_ERROR,"参数错误");
        STATUS_DEFAULT_MESSAGE.put(RESOURCE_NOT_EXIST,"资源不存在");
        STATUS_DEFAULT_MESSAGE.put(DEL_RESOURCE_FAIl,"删除失败");
        STATUS_DEFAULT_MESSAGE.put(REPEATED_RESOURCE_NAME, "资源名重复");
        STATUS_DEFAULT_MESSAGE.put(FILE_FORMAT_ERROR,"文件格式不支持");
        STATUS_DEFAULT_MESSAGE.put(FILE_TOO_LARGE,"文件大小超出限制");
    }

    private Integer code;
    private String message;

    public CommonResponse() {
        this.code = STATUS_OK;
        this.message = STATUS_DEFAULT_MESSAGE.get(STATUS_OK);
    }

    public CommonResponse(Integer code, String message) {
        setCode(code);
        this.message = message;
    }

    public CommonResponse(Integer code) {
        setCode(code);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
        this.message = STATUS_DEFAULT_MESSAGE.get(code);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
