package com.byfan.picamass.controller;

import com.byfan.picamass.common.CommonResponse;
import com.byfan.picamass.common.ObjectResponse;
import com.byfan.picamass.exception.PicServerException;
import com.byfan.picamass.model.UserEntity;
import com.byfan.picamass.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/18 22:38
 */
@Slf4j
@Api(description = "用户接口")
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 新增/修改用户
     * @param user
     * @return
     */
    @ApiOperation(value = "新增/修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "配置id（保存时填写）",required = false,paramType = "query",dataType="int"),
            @ApiImplicitParam(name = "name",value = "用户名",required = true,paramType = "query"),
            @ApiImplicitParam(name = "password",value = "用户密码",required = true,paramType = "query"),
            @ApiImplicitParam(name = "createTime",value = "创建时间(不传)",required = false,paramType = "query")
    })
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ObjectResponse save(UserEntity user){
        ObjectResponse response = new ObjectResponse();
        try {
            UserEntity userEntity = userService.save(user);
            response.setResult(userEntity);
            response.setCode(CommonResponse.STATUS_OK);
            return response;
        } catch (PicServerException e) {
            log.error("save user is except ",e);
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 判断用户登录
     *
     * @param name
     * @param password
     * @return
     */
    @ApiOperation(value = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name",value = "用户名",required = true,paramType = "query"),
            @ApiImplicitParam(name = "password",value = "用户密码",required = true,paramType = "query"),
    })
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public ObjectResponse login(String name,String password){
        ObjectResponse response = new ObjectResponse();
        try {
            if (userService.login(name,password)){
                UserEntity user = userService.findByUserNameAndPwd(name, password);
                response.setResult(user);
                response.setCode(CommonResponse.STATUS_OK);
                return response;
            }else {
                throw new PicServerException("用户名或密码错误",CommonResponse.UNKNOWN_ERROR);
            }
        } catch (PicServerException e) {
            log.error("login user is except ",e);
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

}
