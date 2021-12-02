package com.byfan.picamass.config;

import com.byfan.picamass.bean.DefaultConfigKey;
import com.byfan.picamass.dao.ConfigDao;
import com.byfan.picamass.model.ConfigEntity;
import com.byfan.picamass.service.ConfigService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author: fby
 * @Description 启动时初始化系统默认配置
 * @Version 1.0
 * @Date: 2021/11/28 14:17
 */
@Slf4j
@Component
public class InitConfig implements CommandLineRunner {
    @Autowired
    private ConfigService configService;

    @Autowired
    private ConfigDao configDao;

    @Value("${server.port}")
    private String port;

    @Override
    public void run(String... args) throws Exception {
        log.info("初始化系统默认配置...");
        String serverPathConfigKey = DefaultConfigKey.Server_Path.getConfigKey();
        ConfigEntity serverPathConfig = configService.findAllByUserIdAndConfKey(0, serverPathConfigKey);
        if (serverPathConfig == null || serverPathConfig.getConfKey() == null){
            serverPathConfig = new ConfigEntity();
            serverPathConfig.setConfKey(serverPathConfigKey);
            serverPathConfig.setConfValue(DefaultConfigKey.DEFAULT_CONFIG.get(serverPathConfigKey) + ":" + port);
            serverPathConfig.setUserId(0);
            configService.save(serverPathConfig);
        }

        String folderCoverKey = DefaultConfigKey.Folder_Cover.getConfigKey();
        ConfigEntity folderCoverConfig = configService.findAllByUserIdAndConfKey(0, folderCoverKey);
        if (folderCoverConfig == null || folderCoverConfig.getConfKey() == null){
            folderCoverConfig = new ConfigEntity();
            folderCoverConfig.setConfKey(folderCoverKey);
            folderCoverConfig.setConfValue(DefaultConfigKey.DEFAULT_CONFIG.get(folderCoverKey));
            folderCoverConfig.setUserId(0);
            configService.save(folderCoverConfig);
        }
        log.info("初始化系统默认配置完成...");
    }
}
