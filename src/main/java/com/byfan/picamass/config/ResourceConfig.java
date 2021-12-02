package com.byfan.picamass.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: fby
 * @Description 配置文件资源路径映射类
 * @Version 1.0
 * @Date: 2021/11/25 23:24
 */
//@Configuration
public class ResourceConfig implements WebMvcConfigurer {

    //这个配置类用来添加文件访问的访问路径映射规则
    //由于在实际操作过程中发现，上传图片后无法立即访问到，工程目录已经存在图片但target文件夹下不存在
    //重启项目后便可以访问，但在实际项目运行时不可能上传个文件就重启一下
    //于是设置了文件路径的映射，把文件写入到一个绝对路径下（见UploadController类），然后设置映射来访问
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //需要添加映射的绝对路径（路径最后的/一定要加） System.getProperty("user.dir")  获取项目绝对路径
        String imgPath = System.getProperty("user.dir") + "/src/main/resources/static/";
        //设置映射规则，源路径（ResourceLocations）被设置成可以通过映射路径（ip:port/）来访问
        registry.addResourceHandler("/**").
                addResourceLocations("file:" + imgPath);
    }
}

