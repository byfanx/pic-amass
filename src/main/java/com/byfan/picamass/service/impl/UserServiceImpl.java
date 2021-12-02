package com.byfan.picamass.service.impl;

import com.byfan.picamass.bean.DefaultConfigKey;
import com.byfan.picamass.bean.ImageNameFormat;
import com.byfan.picamass.common.CommonResponse;
import com.byfan.picamass.dao.UserDao;
import com.byfan.picamass.exception.PicServerException;
import com.byfan.picamass.model.ConfigEntity;
import com.byfan.picamass.model.FolderEntity;
import com.byfan.picamass.model.UserEntity;
import com.byfan.picamass.service.ConfigService;
import com.byfan.picamass.service.FolderService;
import com.byfan.picamass.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/18 23:09
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;

    @Autowired
    private FolderService folderService;

    @Autowired
    private ConfigService configService;

    @Value("${server.port}")
    private String port;
    /**
     * 新增/修改用户
     *
     * @param user
     */
    @Override
    public UserEntity save(UserEntity user) throws PicServerException {
        boolean isFirst = false;
        if (user.getId() == null){
            isFirst = true;
        }
        UserEntity userEntity =  userDao.save(user);
        // 如果是新用户，则初始化相关文件夹和配置
        if (isFirst){
            initConfig(user.getId());
        }
        return userEntity;
    }

    /**
     * 登录
     *
     * @param name
     * @param password
     */
    @Override
    public boolean login(String name, String password) throws PicServerException {
        if (name == null || password == null){
            log.error("login name or password is null!");
            throw new PicServerException(CommonResponse.PARAM_ERROR);
        }
        List<UserEntity> allByName = userDao.findAllByName(name);
        if (!CollectionUtils.isEmpty(allByName)){
            UserEntity user = userDao.findAllByNameAndPassword(name, password);
            if (user != null){
                return true;
            }
        }
        return false;
    }

    /**
     * 根据用户id查询
     *
     * @param userId
     * @return
     * @throws PicServerException
     */
    @Override
    public UserEntity findById(Integer userId) throws PicServerException {
        if (userId == null || userId < 1){
            log.error("user findById userId is null!");
            throw new PicServerException(CommonResponse.PARAM_ERROR);
        }
        return userDao.findAllById(userId);
    }

    /**
     * 查询用户
     *
     * @param name
     * @param password
     * @return
     * @throws PicServerException
     */
    @Override
    public UserEntity findByUserNameAndPwd(String name, String password) throws PicServerException {
        if (name == null || password == null){
            log.error("findByUserNameAndPwd name or password is null!");
            throw new PicServerException(CommonResponse.PARAM_ERROR);
        }
        UserEntity user = userDao.findAllByNameAndPassword(name, password);

        return user;
    }

    /**
     * 初始化用户默认配置
     * @param userId
     */
    private void initConfig(Integer userId) throws PicServerException{
        // 新建用户默认文件夹
        FolderEntity folder = new FolderEntity();
        folder.setUserId(userId);
        folder.setDescription("默认文件夹");
        folder.setName("默认文件夹");
        folderService.save(folder);

        // 保存默认文件夹配置
        ConfigEntity default_foler_config = new ConfigEntity();
        default_foler_config.setUserId(userId);
        default_foler_config.setConfKey(DefaultConfigKey.Default_Folder.getConfigKey());
        default_foler_config.setConfValue(folder.getId().toString());
        configService.save(default_foler_config);

        // 保存默认文件命名格式配置
        ConfigEntity image_name_format_config = new ConfigEntity();
        image_name_format_config.setUserId(userId);
        image_name_format_config.setConfKey(DefaultConfigKey.Image_Name_Format.getConfigKey());
        image_name_format_config.setConfValue(String.valueOf(ImageNameFormat.TIME.ordinal()));
        configService.save(image_name_format_config);

        // 保存默认访问地址配置
        ConfigEntity server_path_config = new ConfigEntity();
        server_path_config.setUserId(userId);
        server_path_config.setConfKey(DefaultConfigKey.Server_Path.getConfigKey());
        server_path_config.setConfValue(configService.findAllByUserIdAndConfKey(0,DefaultConfigKey.Server_Path.getConfigKey()).getConfValue() + ":" + port);
        configService.save(server_path_config);
    }

}
