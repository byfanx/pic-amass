package com.byfan.picamass.bean;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: fby
 * @Description 用户配置的key和默认key对应的value
 * @Version 1.0
 * @Date: 2021/11/25 21:52
 */
public enum DefaultConfigKey {
    Image_Name_Format("image_name_format"),
    Server_Path("server_path"),
    Folder_Cover("folder_cover"),

    Default_Folder("default_folder");

    public static final Map<String, String> DEFAULT_CONFIG = new HashMap<String, String>();
    public static String FOLDER_COVER = "default_folder_cover.jpg";
    public static String IMAGE_NAME_FORMAT = String.valueOf(ImageNameFormat.TIME.ordinal());
    public static String SERVER_PATH = "127.0.0.1";

    static {
        DEFAULT_CONFIG.put(Folder_Cover.getConfigKey(),FOLDER_COVER);
        DEFAULT_CONFIG.put(Image_Name_Format.getConfigKey(), IMAGE_NAME_FORMAT);
        DEFAULT_CONFIG.put(Server_Path.getConfigKey(), SERVER_PATH);
    }

    private String configKey;

    DefaultConfigKey(String confKey) {
        this.configKey = confKey;
    }

    public String getConfigKey() {
        return configKey;
    }
}
