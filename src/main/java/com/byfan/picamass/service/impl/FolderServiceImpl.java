package com.byfan.picamass.service.impl;

import com.byfan.picamass.bean.DefaultConfigKey;
import com.byfan.picamass.common.PageData;
import com.byfan.picamass.form.BatchForm;
import com.byfan.picamass.common.CommonResponse;
import com.byfan.picamass.dao.ConfigDao;
import com.byfan.picamass.dao.FolderDao;
import com.byfan.picamass.dao.ImageDao;
import com.byfan.picamass.exception.PicServerException;
import com.byfan.picamass.model.FolderEntity;
import com.byfan.picamass.model.ImageEntity;
import com.byfan.picamass.model.UserEntity;
import com.byfan.picamass.service.FolderService;
import com.byfan.picamass.service.ImageService;
import com.byfan.picamass.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/21 16:35
 */
@Service
@Slf4j
public class FolderServiceImpl implements FolderService {

    @Autowired
    private FolderDao folderDao;

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private ImageService imageService;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigDao configDao;

    /**
     * 创建/修改文件夹
     *
     * @param folder
     * @return
     */
    @Override
    public FolderEntity save(FolderEntity folder) throws PicServerException {
        if (folder.getName() == null || folder.getUserId() == null){
            log.error("save folder name or userId is null!");
            throw new PicServerException(CommonResponse.PARAM_ERROR);
        }
        if (folder.getCoverImage() == null){
            folder.setCoverImage(configDao.findAllByUserIdAndConfKey(0, DefaultConfigKey.Folder_Cover.getConfigKey()).getConfValue());
        }
        return folderDao.save(folder);
    }

    /**
     * 删除文件夹,图片移入默认文件夹
     *
     * @param userId
     * @param folderId
     * @return
     */
    @Override
    public void delete(Integer userId,Integer folderId) throws PicServerException {
        UserEntity userEntity = userService.findById(userId);
        if (null == userEntity){
            log.error("delete folder user is not exist!");
            throw new PicServerException("用户不存在",CommonResponse.RESOURCE_NOT_EXIST);
        }
        if (folderId == null || folderId < 0){
            log.error("delete folder folderId is null!");
            throw new PicServerException(CommonResponse.PARAM_ERROR);
        }
        List<Integer> ids = imageDao.findIdsByFolderID(folderId);
        if (CollectionUtils.isEmpty(ids)){
            imageService.move(new BatchForm(ids, userId, folderId, Integer.parseInt(configDao.findAllByUserIdAndConfKey(userId,DefaultConfigKey.Default_Folder.getConfigKey()).getConfValue())));
        }
        folderDao.deleteById(folderId);
    }

    /**
     * 查看用户全部文件夹
     *
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     * @throws PicServerException
     */
    @Override
    public PageData<FolderEntity> getAllFolder(Integer userId, Integer currentPage, Integer pageSize) throws PicServerException {
        PageData<FolderEntity> pageData = new PageData<>();
        if (userId == null || userId < 0){
            log.error("getAllFolder userId is null!");
            throw new PicServerException(CommonResponse.PARAM_ERROR);
        }
        Pageable pageable = null;
        if (currentPage != null && currentPage > 0){
            pageable = PageRequest.of(currentPage-1,pageSize);
        }
        Page<FolderEntity> all = folderDao.findAllByUserId(userId,pageable);
        pageData.setCurrentPage(currentPage != null ? currentPage : 0);
        pageData.setPageSize(pageSize);
        pageData.setTotal((int) all.getTotalElements());
        pageData.setList(all.getContent());
        return pageData;
    }

    /**
     * 根据用户ID和文件夹id查询
     *
     * @param userId
     * @param folderId
     * @return
     * @throws PicServerException
     */
    @Override
    public FolderEntity findByUserIdAndFolderId(Integer userId, Integer folderId) throws PicServerException {
        if (userId == null || folderId == null){
            log.error("findByUserIdAndFolderId userId or folderId is null!");
            throw new PicServerException(CommonResponse.PARAM_ERROR);
        }
        return folderDao.findByUserIdAndFolder(userId, folderId);
    }
}
