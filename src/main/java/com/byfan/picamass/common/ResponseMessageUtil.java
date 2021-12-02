package com.byfan.picamass.common;

import java.util.Date;

/**
 * @Author: FBY
 * @Description 返回统一的数据格式操作类
 * @Version 1.0
 * @Date: 2021/7/25 23:58
 */
public class ResponseMessageUtil {
    /**
     * 返回失败消息，一般用于增删改操作的结果返回
     * */
    public static ResponseMessage error(String msg){
        ResponseMessage message = new ResponseMessage();
        message.setCode(500);
        message.setMessage(msg);
        message.setTimestamp(new Date().getTime());
        return message;
    }

    /**
     * 返回成功消息，一般用于增删改操作的结果返回
     * */
    public static ResponseMessage success(String msg){
        ResponseMessage message = new ResponseMessage();
        message.setCode(200);
        message.setMessage(msg);
        message.setTimestamp(new Date().getTime());
        return message;
    }

    /**
     * 返回成功的消息，一般用于查询操作的结果返回
     * */
    public static ResponseMessage success(String msg,Object data){
        ResponseMessage message = new ResponseMessage();
        message.setCode(200);
        message.setMessage(msg);
        message.setData(data);
        message.setTimestamp(new Date().getTime());
        return message;
    }

    /**
     * 返回成功的消息，一般用于查询操作的结果返回
     * */
    public static ResponseMessage success(Object data){
        ResponseMessage message = new ResponseMessage();
        message.setCode(200);
        message.setMessage("success");
        message.setData(data);
        message.setTimestamp(new Date().getTime());
        return message;
    }
}
