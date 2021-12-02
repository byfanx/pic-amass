package com.byfan.picamass.service.impl;

import com.byfan.picamass.bean.*;
import com.byfan.picamass.common.CommonResponse;
import com.byfan.picamass.common.PageData;
import com.byfan.picamass.dao.FolderDao;
import com.byfan.picamass.dao.ImageDao;
import com.byfan.picamass.exception.PicServerException;
import com.byfan.picamass.form.BatchForm;
import com.byfan.picamass.model.ConfigEntity;
import com.byfan.picamass.model.FolderEntity;
import com.byfan.picamass.model.ImageEntity;
import com.byfan.picamass.model.UserEntity;
import com.byfan.picamass.service.ConfigService;
import com.byfan.picamass.service.FolderService;
import com.byfan.picamass.service.ImageService;
import com.byfan.picamass.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author: fby
 * @Description
 * @Version 1.0
 * @Date: 2021/11/21 16:54
 */
@Service
@Slf4j
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ImageDao imageDao;

    @Autowired
    private UserService userService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private FolderDao folderDao;

    @Value("${file_path}")
    private String filePath;

    /**
     * 保存图片
     *
     * @param image
     * @throws PicServerException
     */
    @Override
    public ImageEntity save(ImageEntity image) throws PicServerException {
        if (image.getUserId() == null || image.getDir() == null){
            log.error("save image userId or dir is null!");
            throw new PicServerException("userId or dir is null",CommonResponse.PARAM_ERROR);
        }
        image.setStatus(1);
        return imageDao.save(image);
    }

    /**
     * 单文件上传
     *
     * @param uploadBean
     * @throws PicServerException
     */
    @Override
    public ImageEntity upload(UploadBean uploadBean) throws PicServerException, IOException {
        MultipartFile multipartFile = uploadBean.getFile();
        String originalFilename = multipartFile.getOriginalFilename();
        String fileSuffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        String fileName = "default";

        // 参数校验
        uploadBean = validUploadBean(uploadBean);
        // 获取唯一名称
        fileName = getOnlyFileName(uploadBean.getReName(), fileSuffix, uploadBean.getNameFormat());

        String finalFileName = fileName + fileSuffix;
        String dir = filePath + "/" + finalFileName;
        File saveFile = new File(dir);
        OutputStream out = new FileOutputStream(saveFile);
        byte[] bytes = multipartFile.getBytes();
        out.write(bytes);
        out.close();
        log.info("upload file success!");

        ImageEntity imageEntity = new ImageEntity();
        imageEntity.setUserId(uploadBean.getUserId());
        imageEntity.setFolderId(uploadBean.getFolderId());
        imageEntity.setName(finalFileName);
        imageEntity.setDir(filePath + "/");
        imageEntity.setContentType(multipartFile.getContentType());
        imageEntity.setPath(configService.findAllByUserIdAndConfKey(uploadBean.getUserId(), DefaultConfigKey.Server_Path.getConfigKey()).getConfValue());
        imageEntity.setSize(computeSize(multipartFile.getSize()));
        BufferedImage image = ImageIO.read(multipartFile.getInputStream());
        if (image != null){
            imageEntity.setWidth((double) image.getWidth());
            imageEntity.setHeight((double) image.getHeight());
        }

        // 保存图片信息
        return save(imageEntity);
    }

    /**
     * 上传网络图片
     *
     * @param uploadBean
     * @return
     * @throws PicServerException
     * @throws IOException
     */
    @Override
    public ImageEntity uploadFromUrl(UploadBean uploadBean) throws PicServerException {
        // 参数校验
        if (StringUtils.isBlank(uploadBean.getUrl())){
            log.error("uploadFromUrl url is null!");
            throw new PicServerException("url is null",CommonResponse.PARAM_ERROR);
        }
        uploadBean = validUploadBean(uploadBean);

        ImageEntity imageEntity = new ImageEntity();
        try{
            // new一个URL对象
            URL url = new URL(uploadBean.getUrl());
            // 打开链接
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            // 设置请求方式为"GET"
            conn.setRequestMethod("GET");
            // 超时响应时间为5秒
            conn.setConnectTimeout(10 * 1000);

            String contentType = conn.getContentType();
            long fileSize = 0;
            if (StringUtils.isNotBlank(conn.getHeaderField("Content-Length"))){
                fileSize = Long.parseLong(conn.getHeaderField("Content-Length"));
            }
            // 单个文件不超过50M
            if (fileSize/Math.pow(1024,2) > 50){
                log.error("upload file is too large");
                throw new PicServerException(CommonResponse.FILE_TOO_LARGE);
            }
            // 默认文件格式jpg
            String fileSuffix = ".jpg";

            // 从文件类型获取
            if (StringUtils.isNotBlank(contentType)){
                List<String> allowTypes = Arrays.stream(ImageAllowTypes.values()).map(ImageAllowTypes::getType).collect(Collectors.toList());
                if (!allowTypes.contains(contentType)){
                    log.error("uploadFromUrl imageFormat is error!");
                    throw new PicServerException(CommonResponse.FILE_FORMAT_ERROR);
                }
                fileSuffix = "." + ImageAllowTypes.IMAGE_TYPES.get(contentType);
            }
            // 获取文件唯一名称
            String fileName = getOnlyFileName(uploadBean.getReName(), fileSuffix, uploadBean.getNameFormat());
            String finalFileName = fileName + fileSuffix;
            String dir = filePath + "/" + finalFileName;

            // 通过输入流获取图片数据
            InputStream inputStream = conn.getInputStream();
            // 得到图片的二进制数据，以二进制封装得到数据，具有通用性
            byte[] data = readInputStream(inputStream);
            // new一个文件对象用来保存图片，默认保存当前工程根目录
            File imageFile = new File(dir);
            // 创建输出流
            FileOutputStream outStream = new FileOutputStream(imageFile);
            // 写入数据
            outStream.write(data);
            // 关闭输出流
            outStream.close();

            imageEntity.setUserId(uploadBean.getUserId());
            imageEntity.setFolderId(uploadBean.getFolderId());
            imageEntity.setName(finalFileName);
            imageEntity.setDir(filePath + "/");
            imageEntity.setContentType(contentType);
            imageEntity.setPath(configService.findAllByUserIdAndConfKey(uploadBean.getUserId(), DefaultConfigKey.Server_Path.getConfigKey()).getConfValue());
            imageEntity.setSize(computeSize(fileSize));
            // 获取文件宽高
            BufferedImage image = ImageIO.read(inputStream);
            if (image != null){
                imageEntity.setWidth((double) image.getWidth());
                imageEntity.setHeight((double) image.getHeight());
            }
            // 关闭网络链接
            conn.disconnect();
        } catch (IOException io){
            log.error("uploadFromUrl is error!");
            throw new PicServerException("上传失败",CommonResponse.UNKNOWN_ERROR);
        }

        return imageEntity;
    }



    /**
     * 批量放进回收站
     *
     * @param batchForm
     */
    @Override
    public void trash(BatchForm batchForm) throws PicServerException{
        if (null == userService.findById(batchForm.getUserId())){
            log.error("trash user is not exist!");
            throw new PicServerException("用户不存在",CommonResponse.RESOURCE_NOT_EXIST);
        }
        if (CollectionUtils.isNotEmpty(batchForm.getIds())){
            imageDao.trashByIds(batchForm.getIds());
        }
    }

    /**
     * 批量永久删除
     *
     * @param batchForm
     */
    @Override
    public void delete(BatchForm batchForm) throws PicServerException {
        if (null == userService.findById(batchForm.getUserId())){
            log.error("delete image user is not exist!");
            throw new PicServerException("用户不存在",CommonResponse.RESOURCE_NOT_EXIST);
        }
        if (CollectionUtils.isNotEmpty(batchForm.getIds())){
            List<ImageEntity> images = imageDao.findByIds(batchForm.getIds());
            List<String> dirs = images.stream().map(ImageEntity::getDir).collect(Collectors.toList());
            if (!deleteFile(dirs)){
                throw new PicServerException("删除文件失败");
            }
            imageDao.deleteByIds(batchForm.getIds());
        }
    }

    /**
     * 查看用户回收站文件
     *
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     * @throws PicServerException
     */
    @Override
    public PageData<ImageEntity> getTrash(Integer userId, Integer currentPage, Integer pageSize) throws PicServerException {
        PageData<ImageEntity> pageData = new PageData<>();
        if (null == userService.findById(userId)){
            log.error("getTrash image user is not exist!");
            throw new PicServerException("用户不存在",CommonResponse.RESOURCE_NOT_EXIST);
        }
        Pageable pageable = null;
        if (currentPage != null && currentPage > 0){
            pageable = PageRequest.of(currentPage-1,pageSize);
        }
        Page<ImageEntity> all = imageDao.findTrash(userId,pageable);
        pageData.setCurrentPage(currentPage != null ? currentPage : 0);
        pageData.setPageSize(pageSize);
        pageData.setTotal((int) all.getTotalElements());
        pageData.setList(all.getContent());
        return pageData;
    }

    /**
     * 批量恢复
     *
     * @param batchForm
     * @throws PicServerException
     */
    @Override
    public void recovery(BatchForm batchForm) throws PicServerException {
        UserEntity userEntity = userService.findById(batchForm.getUserId());
        if (null == userEntity){
            log.error("recovery image user is not exist;");
            throw new PicServerException("用户不存在",CommonResponse.RESOURCE_NOT_EXIST);
        }

        if (CollectionUtils.isNotEmpty(batchForm.getIds())){
            ConfigEntity config = configService.findAllByUserIdAndConfKey(userEntity.getId(), DefaultConfigKey.Default_Folder.getConfigKey());
            if (config == null){
                FolderEntity folder = new FolderEntity();
                folder.setUserId(userEntity.getId());
                folder.setDescription("默认文件夹");
                folder.setName("默认文件夹");
                folder = folderService.save(folder);

                config = new ConfigEntity();
                config.setUserId(userEntity.getId());
                config.setConfKey(DefaultConfigKey.Default_Folder.getConfigKey());
                config.setConfKey(String.valueOf(folder.getId()));
                config = configService.save(config);
            }
            imageDao.recovery(batchForm.getIds(), Integer.parseInt(config.getConfValue()));
        }
    }

    /**
     * 批量移动
     *
     * @param moveForm
     */
    @Override
    public void move(BatchForm moveForm) throws PicServerException {
        UserEntity userEntity = userService.findById(moveForm.getUserId());
        if (null == userEntity){
            log.error("move user is not exist!");
            throw new PicServerException("用户不存在",CommonResponse.RESOURCE_NOT_EXIST);
        }
        if (moveForm.getOldFolderId() == null || moveForm.getNewFolderId() == null){
            log.error("move oldFolderId or newFolderId is null!");
            throw new PicServerException("oldFolderId or newFolderId is null",CommonResponse.PARAM_ERROR);
        }
        // 判断文件夹是否属于用户
        Page<FolderEntity> allFolder = folderDao.findAllByUserId(userEntity.getId(),null);
        System.err.println(moveForm);
        List<Integer> allIds = allFolder.getContent().stream().map(FolderEntity::getId).collect(Collectors.toList());
        if (!allIds.contains(moveForm.getOldFolderId()) || !allIds.contains(moveForm.getNewFolderId())){
            log.error("move oldFolderId or newFolderId is not exist!");
            throw new PicServerException("文件夹不存在",CommonResponse.RESOURCE_NOT_EXIST);
        }
        if (CollectionUtils.isNotEmpty(moveForm.getIds())){
            imageDao.move(moveForm.getIds(), moveForm.getNewFolderId());
        }
    }

    /**
     * 查看用户全部图片
     * @param userId
     * @param currentPage
     * @param pageSize
     * @return
     * @throws PicServerException
     */
    @Override
    public PageData<ImageEntity> getAll(Integer userId, Integer currentPage, Integer pageSize) throws PicServerException {
        PageData<ImageEntity> pageData = new PageData<>();
        if (null == userService.findById(userId)){
            log.error("getAll image user is not exist!");
            throw new PicServerException("用户不存在",CommonResponse.RESOURCE_NOT_EXIST);
        }
        Pageable pageable = null;
        if (currentPage != null && currentPage > 0){
            pageable = PageRequest.of(currentPage-1,pageSize);
        }
        Page<ImageEntity> all = imageDao.findAllByUserId(userId,pageable);
        pageData.setCurrentPage(currentPage != null ? currentPage : 0);
        pageData.setPageSize(pageSize);
        pageData.setTotal((int) all.getTotalElements());
        pageData.setList(all.getContent());
        return pageData;
    }

    /**
     * 查看用户文件夹下图片
     *
     * @param userId
     * @param folderId
     * @return
     */
    @Override
    public PageData<ImageEntity> findByFolderId(Integer userId, Integer folderId, Integer currentPage, Integer pageSize) throws PicServerException {
        PageData<ImageEntity> pageData = new PageData<>();
        if (userId == null || folderId == null)
        {
            log.error("findByFolderId userId or folderId is null!");
            throw new PicServerException("userId or folderId is null",CommonResponse.PARAM_ERROR);
        }
        UserEntity user = userService.findById(userId);
        if (null == user){
            log.error("findByFolderId user is not exist!");
            throw new PicServerException("用户不存在",CommonResponse.RESOURCE_NOT_EXIST);
        }
        FolderEntity folder = folderService.findByUserIdAndFolderId(userId, folderId);
        if (null == folder){
            log.error("findByFolderId folder is not exist!");
            throw new PicServerException("文件夹不存在",CommonResponse.RESOURCE_NOT_EXIST);
        }
        Pageable pageable = null;
        if (currentPage != null && currentPage > 0){
            pageable = PageRequest.of(currentPage-1,pageSize);
        }
        Page<ImageEntity> allByFolderID = imageDao.findAllByFolderID(folderId,pageable);
        pageData.setCurrentPage(currentPage != null ? currentPage : 0);
        pageData.setPageSize(pageSize);
        pageData.setTotal((int) allByFolderID.getTotalElements());
        pageData.setList(allByFolderID.getContent());
        return pageData;
    }

    /**
     * 校验上传的基本参数
     *
     * @param uploadBean
     * @return
     * @throws PicServerException
     */
    public UploadBean validUploadBean(UploadBean uploadBean) throws PicServerException {
        // 判断用户是否存在
        if (null == userService.findById(uploadBean.getUserId())){
            log.error("upload file user is null!");
            throw new PicServerException("用户不存在",CommonResponse.RESOURCE_NOT_EXIST);
        }
        // 是否是默认文件夹
        if (uploadBean.getFolderId() == null){
            ConfigEntity defaultFolderConfig = configService.findAllByUserIdAndConfKey(uploadBean.getUserId(), DefaultConfigKey.Default_Folder.getConfigKey());
            uploadBean.setFolderId(Integer.parseInt(defaultFolderConfig.getConfValue()));
        }
        // 判断文件夹是否存在
        if (null == folderService.findByUserIdAndFolderId(uploadBean.getUserId(), uploadBean.getFolderId())){
            log.error("upload file folder is null!");
            throw new PicServerException("文件夹不存在",CommonResponse.RESOURCE_NOT_EXIST);
        }
        // 判断文件命名格式
        if (null == uploadBean.getNameFormat() || uploadBean.getNameFormat() < 0 || uploadBean.getNameFormat() >= ImageNameFormat.values().length){
            log.error("upload file nameFormat is null!");
            throw new PicServerException("nameFormat is error",CommonResponse.PARAM_ERROR);
        }
        return uploadBean;
    }

    /**
     * 获取文件唯一名称
     * @param fileName
     * @param fileSuffix
     * @param imageNameFormat
     * @return
     * @throws PicServerException
     */
    public String getOnlyFileName(String fileName,String fileSuffix,Integer imageNameFormat) throws PicServerException {
        String onlyFileName = "";
        if (imageNameFormat == ImageNameFormat.RENAME.ordinal()) {
            // 重命名则不允许名称为空
            if (StringUtils.isBlank(fileName)) {
                log.error("upload fileName is null!");
                throw new PicServerException("fileName is null",CommonResponse.PARAM_ERROR);
            }
            // 不允许重复名称
            if (CollectionUtils.isNotEmpty(imageDao.findAllByName(fileName))) {
                log.error("upload fileName is exit!");
                throw new PicServerException("文件名已存在",CommonResponse.REPEATED_RESOURCE_NAME);
            }
            onlyFileName = fileName;
        } else {
            onlyFileName = ImageNameFormat.values()[imageNameFormat].getFormat();
        }
        // 不允许重复名称
        while (CollectionUtils.isNotEmpty(imageDao.findAllByName(onlyFileName + fileSuffix))) {
            onlyFileName = ImageNameFormat.values()[imageNameFormat].getFormat();
        }
        return onlyFileName;
    }

    /**
     * 计算文件大小
     * @param originalSize
     * @return
     */
    public static String computeSize(long originalSize){
        if (originalSize < 1){
            return null;
        }
        String finalSize = "";
        double size = originalSize/1024;
        if (size < 1024){
            // 四舍五入取整
            long roundSize = Math.round(size);
            finalSize = roundSize + "KB";
        }else {
            // 四舍五入保留一位小数
            size = size/1024;
            BigDecimal bigDecimal = new BigDecimal(size);
            double decimalSize = bigDecimal.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
            finalSize = decimalSize + "MB";
        }
        return finalSize;
    }

    /**
     * 读取数据流
     * @param inputStream
     * @return
     */
    public static byte[] readInputStream(InputStream inputStream){
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            //创建一个Buffer字符串
            byte[] buffer = new byte[1024];
            //每次读取的字符串长度，如果为-1，代表全部读取完毕
            int len = 0;
            //使用一个输入流从buffer里把数据读取出来
            while(true){
                    if (!((len=inputStream.read(buffer)) != -1)) break;
                //用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
                outStream.write(buffer, 0, len);
            }
            //关闭输入流
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //把outStream里的数据写入内存
        return outStream.toByteArray();
    }

    /**
     * 删除本地单个文件
     *
     * @param fileNames：要删除的文件的文件全路径列表
     * @return
     */
    private boolean deleteFile(List<String> fileNames) {
        if (CollectionUtils.isNotEmpty(fileNames)){
            for (String fileName : fileNames){
                File file = new File(fileName);
                // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
                if (file.exists() && file.isFile()) {
                    if (file.delete()) {
                        log.info("删除文件" + fileName + "成功！");
                    } else {
                        log.info("删除文件" + fileName + "失败！");
                    }
                } else {
                    log.info("删除文件失败：" + fileName + "不存在！");
                    return false;
                }
            }
        }
        return true;
    }
}
