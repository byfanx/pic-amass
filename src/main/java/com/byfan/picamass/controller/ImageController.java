package com.byfan.picamass.controller;

import com.byfan.picamass.bean.DefaultConfigKey;
import com.byfan.picamass.bean.ImageAllowTypes;
import com.byfan.picamass.common.PageData;
import com.byfan.picamass.form.BatchForm;
import com.byfan.picamass.bean.UploadBean;
import com.byfan.picamass.common.CommonResponse;
import com.byfan.picamass.common.ObjectResponse;
import com.byfan.picamass.exception.PicServerException;
import com.byfan.picamass.model.ImageEntity;
import com.byfan.picamass.service.ImageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/24 18:07
 */
@Slf4j
@Api(description = "图片接口")
@RestController
@RequestMapping("/api/image")
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * 上传单个文件
     * @param uploadBean
     * @return
     * @throws PicServerException
     */
    @ApiOperation(value = "上传单个文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "file",value = "文件",required = true,paramType = "query"),
            @ApiImplicitParam(name = "nameFormat",value = "文件命名格式",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "folderId",value = "文件夹id",required = false,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "reName",value = "文件重命名",required = false,paramType = "query"),
            @ApiImplicitParam(name = "files",value = "文件列表(不传)",required = false,paramType = "query"),
            @ApiImplicitParam(name = "url",value = "图片url(不传)",required = false,paramType = "query")
    })
    @RequestMapping(value = "/upload",method = RequestMethod.POST)
    public ObjectResponse upload(UploadBean uploadBean) throws PicServerException {
        ObjectResponse response = new ObjectResponse();
        if (uploadBean.getFile() == null){
            log.error("upload file is null!");
            throw new PicServerException("文件不能为空",CommonResponse.PARAM_ERROR);
        }
        // 单个文件不超过50M
        if (uploadBean.getFile().getSize()/Math.pow(1024,2) > 50){
            log.error("upload file is too large!");
            throw new PicServerException(CommonResponse.FILE_TOO_LARGE);
        }

        // 判断文件格式
        List<String> allowTypes = Arrays.stream(ImageAllowTypes.values()).map(ImageAllowTypes::getType).collect(Collectors.toList());
            if (!allowTypes.contains(uploadBean.getFile().getContentType())){
                response.setCode(CommonResponse.FILE_FORMAT_ERROR);
                return response;
        }
        try {
            ImageEntity imageEntity = imageService.upload(uploadBean);
            response.setResult(imageEntity);
            response.setCode(CommonResponse.STATUS_OK);
            return response;
        } catch (IOException e) {
            log.error("upload error ",e);
            response.setCode(CommonResponse.UNKNOWN_ERROR);
            response.setMessage("上传失败");
            return response;
        }
    }

    /**
     * 上传网络图片
     * @param uploadBean
     * @return
     */
    @ApiOperation(value = "上传网络文件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "url",value = "图片url",required = true,paramType = "query"),
            @ApiImplicitParam(name = "nameFormat",value = "文件命名格式",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "folderId",value = "文件夹id",required = false,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "reName",value = "文件重命名",required = false,paramType = "query"),
            @ApiImplicitParam(name = "file",value = "文件(不传)",required = false,paramType = "query"),
            @ApiImplicitParam(name = "files",value = "文件列表(不传)",required = false,paramType = "query"),
    })
    @RequestMapping(value = "/uploadFromUrl",method = RequestMethod.POST)
    public ObjectResponse uploadFormUrl(UploadBean uploadBean) {
        ObjectResponse response = new ObjectResponse();
        try {
            ImageEntity imageEntity = imageService.uploadFromUrl(uploadBean);
            response.setResult(imageEntity);
            response.setCode(CommonResponse.STATUS_OK);
            return response;
        } catch (PicServerException e) {
            log.error("upload error ",e);
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 批量放回回收站
     * @param batchForm
     * @return
     */
    @ApiOperation(value = "批量放回回收站")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "ids",value = "id列表",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "oldFolderId",value = "旧文件夹id(不传)",required = false,paramType = "query"),
            @ApiImplicitParam(name = "newFolderId",value = "新文件夹id(不传)",required = false,paramType = "query"),
    })
    @RequestMapping(value = "/trash",method = RequestMethod.GET)
    public ObjectResponse trash(BatchForm batchForm){
        ObjectResponse response = new ObjectResponse();
        try {
            imageService.trash(batchForm);
            response.setCode(CommonResponse.STATUS_OK);
            return response;
        } catch (PicServerException e) {
            log.error("trash image error ",e);
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 查看用户回收站图片
     * @param userId
     * @return
     */
    @ApiOperation(value = "查看用户回收站图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "currentPage",value = "当前页",required = false,paramType = "query",dataType = "int",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",value = "每页数量",required = false,paramType = "query",dataType = "int",defaultValue = "20"),
    })
    @RequestMapping(value = "/getTrash",method = RequestMethod.GET)
    public ObjectResponse getTrash(@RequestParam Integer userId,
                                   @RequestParam(required = false) Integer currentPage,
                                   @RequestParam(required = false,defaultValue = "20") Integer pageSize){
        ObjectResponse response = new ObjectResponse();
        try {
            PageData<ImageEntity> all = imageService.getTrash(userId,currentPage,pageSize);
            response.setResult(all);
            response.setCode(CommonResponse.STATUS_OK);
            return response;
        } catch (PicServerException e) {
            log.error("getTrash image is error!");
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 从回收站批量恢复
     * @param batchForm
     * @return
     */
    @ApiOperation(value = "从回收站批量恢复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "ids",value = "id列表",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "oldFolderId",value = "旧文件夹id(不传)",required = false,paramType = "query"),
            @ApiImplicitParam(name = "newFolderId",value = "新文件夹id(不传)",required = false,paramType = "query"),
    })
    @RequestMapping(value = "/recovery",method = RequestMethod.GET)
    public ObjectResponse recovery(BatchForm batchForm){
        ObjectResponse response = new ObjectResponse();
        try {
            imageService.recovery(batchForm);
            response.setCode(CommonResponse.STATUS_OK);
            return response;
        } catch (PicServerException e) {
            log.error("recovery image is error!");
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 批量永久删除
     * @param batchForm
     * @return
     */
    @ApiOperation(value = "批量永久删除")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "ids",value = "id列表",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "oldFolderId",value = "旧文件夹id(不传)",required = false,paramType = "query"),
            @ApiImplicitParam(name = "newFolderId",value = "新文件夹id(不传)",required = false,paramType = "query"),
    })
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public ObjectResponse delete(BatchForm batchForm){
        ObjectResponse response = new ObjectResponse();
        try {
            imageService.delete(batchForm);
            response.setCode(CommonResponse.STATUS_OK);
            return response;
        } catch (PicServerException e) {
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 批量移动
     * @param moveForm
     * @return
     */
    @ApiOperation(value = "批量移动")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "ids",value = "id列表",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "oldFolderId",value = "旧文件夹id",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "newFolderId",value = "新文件夹id",required = true,paramType = "query",dataType = "int"),
    })
    @RequestMapping(value = "/move",method = RequestMethod.GET)
    public ObjectResponse move(BatchForm moveForm){
        ObjectResponse response = new ObjectResponse();
        try {
            imageService.move(moveForm);
            response.setCode(CommonResponse.STATUS_OK);
            return response;
        } catch (PicServerException e) {
            log.error("move image is error!");
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 查看用户所有图片
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查看用户所有图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "currentPage",value = "当前页",required = false,paramType = "query",dataType = "int",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",value = "每页数量",required = false,paramType = "query",dataType = "int",defaultValue = "20"),
    })
    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    public ObjectResponse getAll(@RequestParam Integer userId,
                                 @RequestParam(required = false) Integer currentPage,
                                 @RequestParam(required = false,defaultValue = "20") Integer pageSize){
        ObjectResponse response = new ObjectResponse();
        try {
            PageData<ImageEntity> all = imageService.getAll(userId,currentPage,pageSize);
            response.setResult(all);
            response.setCode(CommonResponse.STATUS_OK);
            return response;
        } catch (PicServerException e) {
            log.error("getAll image is error!");
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 查看用户文件夹下图片
     * @param userId
     * @param folderId
     * @param currentPage
     * @param pageSize
     * @return
     */
    @ApiOperation(value = "查看用户文件夹下图片")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "folderId",value = "文件夹id",required = true,paramType = "query",dataType = "int"),
            @ApiImplicitParam(name = "currentPage",value = "当前页",required = false,paramType = "query",dataType = "int",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",value = "每页数量",required = false,paramType = "query",dataType = "int",defaultValue = "20"),
    })
    @RequestMapping(value = "/getByFolder",method = RequestMethod.GET)
    public ObjectResponse getByFolder(@RequestParam Integer userId,
                                      @RequestParam Integer folderId,
                                      @RequestParam(required = false) Integer currentPage,
                                      @RequestParam(required = false,defaultValue = "20") Integer pageSize){
        ObjectResponse response = new ObjectResponse();
        try {
            PageData<ImageEntity> all = imageService.findByFolderId(userId,folderId,currentPage,pageSize);
            response.setResult(all);
            response.setCode(CommonResponse.STATUS_OK);
            return response;
        } catch (PicServerException e) {
            log.error("getByFolder image is error!");
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

}
