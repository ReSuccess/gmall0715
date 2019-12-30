package com.atguigu.gmall0715.manage.controller;

import org.apache.commons.lang3.StringUtils;
import org.csource.common.MyException;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * 上传图片到远程搭建的fastDfs服务器
 *  — 192.168.116.130 服务器
 * @author sujie
 * @date 2019-12-29-21:41
 */
@RestController
@CrossOrigin
public class FileUploadController {
    @Value("${fileServer.url}")
    private String filePath;

    @PostMapping(value = "fileUpload")
    public String fileUpload(MultipartFile file) throws IOException, MyException {
        String configFile = this.getClass().getResource("/tracker.conf").getFile();
        ClientGlobal.init(configFile);
        TrackerClient trackerClient=new TrackerClient();
        TrackerServer trackerServer=trackerClient.getConnection();
        StorageClient storageClient=new StorageClient(trackerServer,null);
       //文件名
        String orginalFilename = file.getOriginalFilename();
       //文件后缀
        String ext = StringUtils.substringAfterLast(orginalFilename, ".");
        //上传图片，返回存储路径
        String[] uploadFile = storageClient.upload_file(file.getBytes(), ext, null);
        //拼接文件存储路径
        StringBuilder path = new StringBuilder(filePath);
        for (String str: uploadFile) {
            path.append(str);
        }
        return path.toString();
    }
}
