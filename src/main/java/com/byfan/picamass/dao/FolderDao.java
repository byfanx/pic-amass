package com.byfan.picamass.dao;

import com.byfan.picamass.model.FolderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/21 16:31
 */
public interface FolderDao extends BaseRepository<FolderEntity,Integer>{

    @Query("select f from FolderEntity f where f.userId=:userId order by f.createTime desc ")
    Page<FolderEntity> findAllByUserId(@Param("userId") Integer userId,
                                       Pageable pageable);

    @Query("select f from FolderEntity f where f.id=:id")
    FolderEntity findAllById(@Param("id") Integer id);

    @Query("select f from FolderEntity f where f.id=:folderId and f.userId=:userId")
    FolderEntity findByUserIdAndFolder(@Param("userId") Integer userId,@Param("folderId") Integer folderId);



}
