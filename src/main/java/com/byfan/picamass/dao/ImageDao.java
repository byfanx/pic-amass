package com.byfan.picamass.dao;

import com.byfan.picamass.model.ImageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/21 16:51
 */
public interface ImageDao extends BaseRepository<ImageEntity,Integer>{

    /**
     * 根据id查询图片
     * @param id
     * @return
     */
    @Query("select i from ImageEntity i where i.id=:id")
    ImageEntity findAllById(@Param("id") Integer id);

    /**
     * 查看文件夹内图片
     * @param folderId
     * @return
     */
    @Query("select i from ImageEntity i where i.folderId=:folderId and i.status = 1 order by i.uploadTime desc ")
    Page<ImageEntity> findAllByFolderID(@Param("folderId") Integer folderId,
                                        Pageable pageable);

    /**
     * 查询文件夹内图片id列表
     * @param folderId
     * @return
     */
    @Query("select i.id from ImageEntity i where i.folderId=:folderId and i.status = 1 order by i.uploadTime desc ")
    List<Integer> findIdsByFolderID(@Param("folderId") Integer folderId);

    /**
     * 查看回收站内图片
     * @param userId
     * @return
     */
    @Query("select i from ImageEntity i where i.userId=:userId and i.status = 0 order by i.uploadTime desc ")
    Page<ImageEntity> findTrash(@Param("userId") Integer userId,
                                Pageable pageable);

    /**
     * 查看用户所有图片
     * @param userId
     * @return
     */
    @Query("select i from ImageEntity i where i.userId=:userId and i.status = 1 order by i.uploadTime desc ")
    Page<ImageEntity> findAllByUserId(@Param("userId") Integer userId,
                                      Pageable pageable);

    /**
     * 根据名称查询图片
     * @param name
     * @return
     */
    @Query("select i from ImageEntity i where i.name=:name")
    List<ImageEntity> findAllByName(@Param("name") String name);

    /**
     * 删除至回收站
     * @param ids
     */
    @Transactional
    @Modifying
    @Query("update ImageEntity set status=0,folderId=-1 where id in (:ids)")
    void trashByIds(@Param("ids") List<Integer> ids);

    /**
     * 批量恢复，从回收站移到默认文件夹
     * @param ids
     * @param folderId
     */
    @Transactional
    @Modifying
    @Query("update ImageEntity set status=1,folderId=:folderId where id in (:ids)")
    void recovery(@Param("ids") List<Integer> ids,@Param("folderId") Integer folderId);

    /**
     * 永久删除
     * @param ids
     */
    @Transactional
    @Modifying
    @Query("delete from ImageEntity where id in (:ids)")
    void deleteByIds(@Param("ids") List<Integer> ids);

    /**
     * 根据id列表查询
     * @param ids
     * @return
     */
    @Query("select i from ImageEntity i where i.id in (:ids)")
    List<ImageEntity> findByIds(@Param("ids") List<Integer> ids);

    /**
     * 批量移动
     * @param ids
     * @param folderId
     */
    @Transactional
    @Modifying
    @Query("update ImageEntity set folderId=:folderID where id in (:ids)")
    void move(@Param("ids") List<Integer> ids,@Param("folderID") Integer folderId);

}
