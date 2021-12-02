package com.byfan.picamass.service;

import com.byfan.picamass.exception.PicServerException;
import com.byfan.picamass.model.ConfigEntity;

import java.util.List;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/23 11:00
 */
public interface ConfigService {

    /**
     * 新增/修改配置
     * @param config
     * @return
     */
    ConfigEntity save(ConfigEntity config) throws PicServerException;

    /**
     * 根据用户id获取配置
     * @param userId
     * @param configKey
     * @return
     * @throws PicServerException
     */
    ConfigEntity findAllByUserIdAndConfKey(Integer userId,String configKey) throws PicServerException;

    /**
     * 获取用户全部配置
     * @param userId
     * @return
     * @throws PicServerException
     */
    List<ConfigEntity> findAllByUserId(Integer userId) throws PicServerException;
}
