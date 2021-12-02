package com.byfan.picamass.dao;

import com.byfan.picamass.model.UserEntity;

import java.util.List;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/18 22:54
 */
public interface UserDao extends BaseRepository<UserEntity,Integer> {

    /**
     * 根据名称查询用户
     * @param name
     * @return
     */
    List<UserEntity> findAllByName(String name);

    /**
     * 根据名称和密码查询用户
     * @param name
     * @param password
     * @return
     */
    UserEntity findAllByNameAndPassword(String name,String password);

    /**
     * 根据id查询用户
     * @param userId
     * @return
     */
    UserEntity findAllById(Integer userId);
}
