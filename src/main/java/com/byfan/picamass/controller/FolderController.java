package com.byfan.picamass.controller;

import com.byfan.picamass.bean.ImageNameFormat;
import com.byfan.picamass.common.CommonResponse;
import com.byfan.picamass.common.ObjectResponse;
import com.byfan.picamass.common.PageData;
import com.byfan.picamass.exception.PicServerException;
import com.byfan.picamass.model.FolderEntity;
import com.byfan.picamass.service.FolderService;
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

import java.util.List;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/24 09:41
 */
@Slf4j
@Api(description = "文件夹接口")
@RestController
@RequestMapping("/api/folder")
public class FolderController {
    @Autowired
    private FolderService folderService;

    /**
     * 新增/修改文件夹
     * @param folder
     * @return
     */
    @ApiOperation("新增/修改用户文件夹")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "配置id（保存时填写）",required = false,paramType = "query",dataType="int"),
            @ApiImplicitParam(name = "userId",value = "用户的id",required = true,paramType = "query",dataType="int"),
            @ApiImplicitParam(name = "name",value = "文件夹名称",required = true,paramType = "query"),
            @ApiImplicitParam(name = "description",value = "文件夹描述",required = false,paramType = "query"),
            @ApiImplicitParam(name = "coverImage",value = "文件夹封面",required = false,paramType = "query"),
            @ApiImplicitParam(name = "createTime",value = "创建时间(不传)",required = false,paramType = "query")
    })
    @RequestMapping(value = "/save",method = RequestMethod.POST)
    public ObjectResponse save(FolderEntity folder){
        ObjectResponse response = new ObjectResponse();
        try{
            folderService.save(folder);
            response.setCode(CommonResponse.STATUS_OK);
            return response;
        } catch (PicServerException e){
            log.error("save folder is except ",e);
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 删除文件夹
     * @param folderId
     * @return
     */
    @ApiOperation("删除用户文件夹")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,paramType = "query",dataType="int"),
            @ApiImplicitParam(name = "folderId",value = "文件夹id",required = true,paramType = "query",dataType="int")
    })
    @RequestMapping(value = "/delete",method = RequestMethod.DELETE)
    public ObjectResponse delete(Integer userId,Integer folderId){
        ObjectResponse response = new ObjectResponse();
        try {
            folderService.delete(userId,folderId);
            response.setCode(CommonResponse.STATUS_OK);
            return response;
        } catch (PicServerException e) {
            log.error("delete folder is except ",e);
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

    /**
     * 查看用户所有文件夹
     * @param userId
     * @return
     */
    @ApiOperation("查看用户全部文件夹")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户id",required = true,paramType = "query",dataType="int"),
            @ApiImplicitParam(name = "currentPage",value = "当前页",required = false,paramType = "query",dataType = "int",defaultValue = "1"),
            @ApiImplicitParam(name = "pageSize",value = "每页数量",required = false,paramType = "query",dataType = "int",defaultValue = "20"),
    })
    @RequestMapping(value = "/getAll",method = RequestMethod.GET)
    public ObjectResponse getAll(@RequestParam Integer userId,
                                 @RequestParam(required = false) Integer currentPage,
                                 @RequestParam(required = false,defaultValue = "20") Integer pageSize){
        ObjectResponse response = new ObjectResponse();
        try {
            PageData<FolderEntity> allFolder = folderService.getAllFolder(userId,currentPage,pageSize);
            response.setResult(allFolder);
            response.setCode(CommonResponse.STATUS_OK);
            return response;
        } catch (PicServerException e) {
            log.error("getAll folder is except ",e);
            response.setCode(e.getErrorCode());
            response.setMessage(e.getMessage());
            return response;
        }
    }

}
