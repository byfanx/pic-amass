package com.byfan.picamass.bean;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/23 11:05
 */
public enum ImageNameFormat {
    RENAME("default"),
    TIME(getTime()),
    REVERSAL_TIME(getReversalTime()),
    UUID16(getUuid16());

    public static final String TIME_Format = "yyyyMMddHHmmssSS";
    public static final String REVERSAL_TIME_Format = "SSssmmHHddMMyyyy";

    private String format;

    private ImageNameFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }


    /**
     * 获取时间字符串
     * @return
     */
    private static String getTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_Format);
        return sdf.format(date);
    }

    /**
     * 获取反向时间字符串
     * @return
     */
    private static String getReversalTime(){
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(REVERSAL_TIME_Format);
        return sdf.format(date);
    }

    /**
     * 获取16位的uuid
     * @return
     */
    private static String getUuid16(){
        String uuid16 = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        return uuid16;
    }

    @Override
    public String toString() {
        return "ImageNameFormat{" +
                "format='" + format + '\'' +
                '}';
    }
}
