package com.byfan.picamass.service;

import com.byfan.picamass.common.PageData;
import com.byfan.picamass.form.BatchForm;
import com.byfan.picamass.bean.UploadBean;
import com.byfan.picamass.exception.PicServerException;
import com.byfan.picamass.model.ImageEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.List;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/21 16:54
 */
public interface ImageService {

    /**
     * 保存图片
     * @param image
     * @throws PicServerException
     */
    ImageEntity save(ImageEntity image) throws PicServerException;

    /**
     * 单文件上传
     * @param uploadBean
     * @throws PicServerException
     */
    ImageEntity upload(UploadBean uploadBean) throws PicServerException, IOException;

    /**
     * 上传网络图片
     * @param uploadBean
     * @return
     * @throws PicServerException
     * @throws IOException
     */
    ImageEntity uploadFromUrl(UploadBean uploadBean) throws PicServerException;

    /**
     * 批量放进回收站
     * @param batchForm
     */
    void trash(BatchForm batchForm) throws PicServerException;

    /**
     * 批量永久删除
     * @param batchForm
     */
    void delete(BatchForm batchForm) throws PicServerException;

    /**
     * 查看用户回收站文件
     *
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     * @throws PicServerException
     */
    PageData<ImageEntity> getTrash(Integer userId, Integer currentPage, Integer pageSize) throws PicServerException;

    /**
     * 批量恢复
     * @param batchForm
     * @throws PicServerException
     */
    void recovery(BatchForm batchForm) throws PicServerException;

    /**
     * 批量移动
     * @param moveForm
     */
    void move(BatchForm moveForm) throws PicServerException;

    /**
     * 查看用户全部图片
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     * @throws PicServerException
     */
    PageData<ImageEntity> getAll(Integer userId, Integer currentPage, Integer pageSize) throws PicServerException;

    /**
     * 查看用户文件夹下图片
     * @param userId
     * @param folderId
     * @return
     */
    PageData<ImageEntity> findByFolderId(Integer userId, Integer folderId, Integer currentPage, Integer pageSize) throws PicServerException;



}
