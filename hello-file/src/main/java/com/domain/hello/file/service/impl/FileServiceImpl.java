package com.domain.hello.file.service.impl;

import com.domain.hello.file.service.FileService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @description:
 * @author: domain
 * @create: 2019/9/28 15:13
 */

@Service
public class FileServiceImpl implements FileService {
    @Override
    public void download(HttpServletResponse response) throws IOException {
        // 文件地址，真实环境是存放在数据库中的
        File file = new File("D:\\upload\\fd5dfc1b-961e-4475-9b12-2d42d34c0df9liuyifei.jpg");
        // 创建输入对象
        FileInputStream fis = new FileInputStream(file);
        // 设置相关格式
        response.setContentType("application/force-download");
        // 设置下载后的文件名以及header
        response.addHeader("Content-disposition", "attachment;fileName=" + "fd5dfc1b-961e-4475-9b12-2d42d34c0df9liuyifei.jpg");
        // 创建输出对象
        OutputStream os = response.getOutputStream();
        // 常规操作
        byte[] buf = new byte[1024];
        int len = 0;
        while((len = fis.read(buf)) != -1) {
            os.write(buf, 0, len);
        }
        fis.close();
    }
}
