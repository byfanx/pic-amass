package com.byfan.picamass.bean;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @Author: fby
 * @Description 前端上传文件传的参数
 * @Version 1.0
 * @Date: 2021/11/25 11:41
 */
@Data
@ToString
public class UploadBean {
    // 单个文件
    MultipartFile file;
    // 多个文件
    MultipartFile[] files;
    // 用户id
    Integer userId;
    // 文件夹id
    Integer folderId;
    // 文件命名格式
    Integer nameFormat = ImageNameFormat.TIME.ordinal();
    // 重命名的名称
    String reName;
    // 上传网络图片的url
    String url;
}
