package com.byfan.picamass.form;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * @Author: fby
 * @Description 批量操作类
 * @Version 1.0
 * @Date: 2021/11/28 11:56
 */
@Data
@ToString
public class BatchForm {
    // id列表
    private List<Integer> ids;
    // 用户id
    private Integer userId;
    // 移动的原文件夹id
    private Integer oldFolderId;
    // 新文件夹id
    private Integer newFolderId;

    public BatchForm() {}

    public BatchForm(List<Integer> ids, Integer userId, Integer oldFolderId, Integer newFolderId) {
        this.ids = ids;
        this.userId = userId;
        this.oldFolderId = oldFolderId;
        this.newFolderId = newFolderId;
    }
}
