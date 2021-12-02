package com.byfan.picamass.controller;

import com.byfan.picamass.bean.DefaultConfigKey;
import com.byfan.picamass.common.CommonResponse;
import com.byfan.picamass.common.ObjectResponse;
import com.byfan.picamass.exception.PicServerException;
import com.byfan.picamass.model.ConfigEntity;
import com.byfan.picamass.service.ConfigService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/23 21:57
 */
@Slf4j
@Api(description = "用户配置接口")
@RestController
@RequestMapping("/api/config")
public class ConfigController {

    @Autowired
    private ConfigService configService;

    /**
     * 新增/修改用户配置
     * @param config
     * @return
     */
    @ApiOperation("新增/修改用户配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "配置id（保存时填写）",required = false,paramType = "query",dataType="int"),
            @ApiImplicitParam(name = "confKey",value = "配置的key",required = true,paramType = "query",dataType="int"),
            @ApiImplicitParam(name = "confValue",value = "配置的值",required = true,paramType = "query"),
            @ApiImplicitParam(name = "userId",value = "用户的id",required = true,paramType = "query",dataType="int"),
            @ApiImplicitParam(name = "createTime",value = "创建时间(不传)",required = false,paramType = "query"),
    })
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ObjectResponse save(ConfigEntity config){
        ObjectResponse response  = new ObjectResponse();
        try {
            configService.save(config);
            response.setCode(CommonResponse.STATUS_OK);return response;
        } catch (PicServerException e) {
            log.error("save config is except ",e);
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 获取用户配置
     * @param userId
     * @param configKey
     * @return
     */
    @ApiOperation("获取用户配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户的id",required = true,paramType = "query",dataType="int"),
            @ApiImplicitParam(name = "configKey",value = "配置的key(0:文件命名配置,1:访问地址配置)",required = true,paramType = "query",dataType = "int")
    })
    @RequestMapping(value = "/getConfig",method = RequestMethod.POST)
    public ObjectResponse getConfig(Integer userId, Integer configKey){
        ObjectResponse response  = new ObjectResponse();
        try {
            // 获取key
            String key = DefaultConfigKey.values()[configKey].getConfigKey();
            ConfigEntity config = configService.findAllByUserIdAndConfKey(userId, key);
            response.setCode(CommonResponse.STATUS_OK);
            response.setResult(config);
            return response;
        } catch (PicServerException e) {
            log.error("getConfig is except ",e);
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    @ApiOperation("获取用户全部配置")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId", value = "用户的id", required = true, paramType = "query", dataType = "int"),
    })
    @RequestMapping(value = "/getAllConfig",method = RequestMethod.POST)
    public ObjectResponse getAllConfig(Integer userId){
        ObjectResponse response  = new ObjectResponse();
        try {
            List<ConfigEntity> configs = configService.findAllByUserId(userId);
            response.setCode(CommonResponse.STATUS_OK);
            response.setResult(configs);
            return response;
        } catch (PicServerException e) {
            log.error("getAllConfig is except ",e);
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }


}
