package com.byfan.picamass.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author: fby
 * @Description 拦截器
 * @Version 1.0
 * @Date: 2021/11/25 14:26
 */
@Slf4j
//@Component
public class AuthInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info(">>>AuthInterceptor>>>>>>>在请求处理之前进行调用>>>>>>>>>");
        log.error(request.getRequestURI());
        log.error(request.getMethod());
        log.error(request.getServletPath());

        log.error(request.getParameterMap().toString());
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }
}
