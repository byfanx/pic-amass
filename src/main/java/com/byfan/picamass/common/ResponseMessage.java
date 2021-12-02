package com.byfan.picamass.common;

import lombok.Data;

/**
 * @Author: FBY
 * @Description 返回统一的数据格式
 * @Version 1.0
 * @Date: 2021/7/25 23:56
 */
@Data
public class ResponseMessage {
    /**
     * 状态码  200 成功  500 错误
     */
    private Integer code;

    /**
     * 信息
     */
    private String message;
    /**
     * 操作结果，返回的数据
     */
    private Object data;

    /**
     * 时间戳
     */
    private Long timestamp;
}
