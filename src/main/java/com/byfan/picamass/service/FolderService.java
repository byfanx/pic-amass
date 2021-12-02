package com.byfan.picamass.service;

import com.byfan.picamass.common.PageData;
import com.byfan.picamass.exception.PicServerException;
import com.byfan.picamass.model.FolderEntity;

import java.util.List;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/21 16:33
 */
public interface FolderService {

    /**
     * 创建/修改文件见
     * @param folder
     * @return
     */
    FolderEntity save(FolderEntity folder) throws PicServerException;

    /**
     * 删除文件夹
     * @param userId
     * @param folderId
     * @return
     */
    void delete(Integer userId,Integer folderId) throws PicServerException;

    /**
     * 查看用户全部文件夹
     *
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     * @throws PicServerException
     */
    PageData<FolderEntity> getAllFolder(Integer userId, Integer currentPage, Integer pageSize) throws PicServerException;

    /**
     * 根据用户ID和文件夹id查询
     * @param userId
     * @param folderId
     * @return
     * @throws PicServerException
     */
    FolderEntity findByUserIdAndFolderId(Integer userId,Integer folderId) throws PicServerException;
}
