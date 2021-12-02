package com.byfan.picamass.service;

import com.byfan.picamass.exception.PicServerException;
import com.byfan.picamass.model.UserEntity;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/18 23:09
 */
public interface UserService {

    /**
     * 新增/保存用户
     * @param user
     * @return
     * @throws PicServerException
     */
    UserEntity save(UserEntity user) throws PicServerException;

    /**
     * 判断登录
     * @param name
     * @param password
     * @return
     * @throws PicServerException
     */
    boolean login(String name,String password) throws PicServerException;

    /**
     * 根据用户id查询
     * @param userId
     * @return
     * @throws PicServerException
     */
    UserEntity findById(Integer userId) throws PicServerException;

    /**
     * 查询用户
     *
     * @param name
     * @param password
     * @return
     * @throws PicServerException
     */
    UserEntity findByUserNameAndPwd(String name, String password) throws PicServerException;
}