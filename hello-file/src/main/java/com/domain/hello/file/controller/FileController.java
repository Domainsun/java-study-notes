package com.domain.hello.file.controller;

import com.domain.hello.file.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

/**
 * @description:
 * @author: domain
 * @create: 2019/9/28 14:46
 */

@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    FileService fileService;

        @PostMapping("upload")
        public String upload (@RequestParam("file") MultipartFile file) {
            // 获取原始名字
            String fileName = file.getOriginalFilename();
            // 获取后缀名
            // String suffixName = fileName.substring(fileName.lastIndexOf("."));
            // 文件保存路径
            String filePath = "d:/upload/";
            // 文件重命名，防止重复
            fileName = filePath + UUID.randomUUID() + fileName;
            // 文件对象
            File dest = new File(fileName);
            // 判断路径是否存在，如果不存在则创建
            if(!dest.getParentFile().exists()) {
                dest.getParentFile().mkdirs();
            }
            try {
                // 保存到服务器中
                file.transferTo(dest);
                return "上传成功";
            } catch (Exception e) {
                e.printStackTrace();
            }
            return "上传失败";
        }

        @GetMapping("download")
        public void download(HttpServletResponse response) throws IOException {
            fileService.download(response);
        }

}
