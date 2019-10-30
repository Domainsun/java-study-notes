package com.domain.hello.file.service;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @description:
 * @author: domain
 * @create: 2019/9/28 15:12
 */

public interface FileService {

    void download(HttpServletResponse response) throws IOException;
}
