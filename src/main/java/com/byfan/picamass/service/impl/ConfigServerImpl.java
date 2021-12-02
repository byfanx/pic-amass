package com.byfan.picamass.service.impl;

import com.byfan.picamass.bean.DefaultConfigKey;
import com.byfan.picamass.common.CommonResponse;
import com.byfan.picamass.dao.ConfigDao;
import com.byfan.picamass.exception.PicServerException;
import com.byfan.picamass.model.ConfigEntity;
import com.byfan.picamass.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/23 11:00
 */
@Service
@Slf4j
public class ConfigServerImpl implements ConfigService {

    @Autowired
    private ConfigDao configDao;

    /**
     * 新增/修改配置
     *
     * @param config
     * @return
     */
    @Override
    public ConfigEntity save(ConfigEntity config) throws PicServerException {
        if (config.getUserId() == null ||config.getConfKey() == null || config.getConfValue() == null){
            log.error("save config userId or key or value is null!");
            throw new PicServerException(CommonResponse.PARAM_ERROR);
        }
        return configDao.save(config);
    }

    /**
     * 根据用户id获取配置
     *
     * @param userId
     * @param configKey
     * @return
     * @throws PicServerException
     */
    @Override
    public ConfigEntity findAllByUserIdAndConfKey(Integer userId, String configKey) throws PicServerException {
        if (userId == null || configKey == null){
            log.error("findConfigByUserId userId or configKey is null!");
            throw new PicServerException(CommonResponse.PARAM_ERROR);
        }
        return configDao.findAllByUserIdAndConfKey(userId, configKey);
    }

    /**
     * 获取用户全部配置
     *
     * @param userId
     * @return
     * @throws PicServerException
     */
    @Override
    public List<ConfigEntity> findAllByUserId(Integer userId) throws PicServerException {
        if (userId == null){
            log.error("findAllByUserId userId is null!");
            throw new PicServerException(CommonResponse.PARAM_ERROR);
        }
        List<ConfigEntity> allConfigs = configDao.getAllByUserId(userId);
        return allConfigs;
    }
}
