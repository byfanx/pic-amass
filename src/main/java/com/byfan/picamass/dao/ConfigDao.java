package com.byfan.picamass.dao;

import com.byfan.picamass.model.ConfigEntity;

import java.util.List;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/21 16:41
 */
public interface ConfigDao extends BaseRepository<ConfigEntity,Integer>{

    /**
     * 根据key获取用户配置
     * @param userId
     * @param key
     * @return
     */
    ConfigEntity findAllByUserIdAndConfKey(Integer userId,String key);

    /**
     * 查看用户所有配置
     * @param userId
     * @return
     */
    List<ConfigEntity> getAllByUserId(Integer userId);
}
