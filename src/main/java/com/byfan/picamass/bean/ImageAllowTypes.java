package com.byfan.picamass.bean;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: fby
 * @Description 支持的图片格式
 * @Version 1.0
 * @Date: 2021/11/27 00:01
 */
public enum ImageAllowTypes {
    JPEG("image/jpeg"),
    PNG("image/png"),
    GIF("image/gif");

    private String type;

    private ImageAllowTypes(String type) {
        this.type = type;
    }

    public String getType(){
        return type;
    }

    public static final Map<String, String> IMAGE_TYPES = new HashMap<String, String>();

    static {
        IMAGE_TYPES.put(JPEG.getType(),"jpeg");
        IMAGE_TYPES.put(PNG.getType(), "png");
        IMAGE_TYPES.put(GIF.getType(), "gif");
    }

}
