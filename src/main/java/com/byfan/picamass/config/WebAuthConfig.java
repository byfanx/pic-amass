package com.byfan.picamass.config;

import com.byfan.picamass.config.AuthInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: fby
 * @Description 配置拦截器
 * @Version 1.0
 * @Date: 2021/11/25 14:29
 */
//@Configuration
public class WebAuthConfig extends WebMvcConfigurationSupport {
    @Resource
    AuthInterceptor authInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {//放行路径
        List<String> myExcludePatterns = new ArrayList();
        // druid
        myExcludePatterns.add("/druid/**");
        // swagger
        myExcludePatterns.add("/webjars/**");
        myExcludePatterns.add("/swagger/**");
        myExcludePatterns.add("/v2/**");
        myExcludePatterns.add("/swagger-ui.html/**");
        myExcludePatterns.add("/swagger-resources/**");
        //系统静态资源的放行  前后端分离项目不用考虑静态资源的放行，只需要验权即可
//        myExcludePatterns.add("/**");
        myExcludePatterns.add("/index.html");
        myExcludePatterns.add("/css/**");
        myExcludePatterns.add("/js/**");

        myExcludePatterns.add("/image/**");

        registry.addInterceptor(authInterceptor).addPathPatterns("/api/**").excludePathPatterns(myExcludePatterns);
    }



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 静态资源位置
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
        // 放行 SWAGGER
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
