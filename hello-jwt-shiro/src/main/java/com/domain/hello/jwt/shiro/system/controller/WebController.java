package com.domain.hello.jwt.shiro.system.controller;

import com.domain.hello.jwt.shiro.core.exception.BusinessException;
import com.domain.hello.jwt.shiro.core.utils.JWTUtil;
import com.domain.hello.jwt.shiro.domain.ResponseBean;
import com.domain.hello.jwt.shiro.domain.UserBean;
import com.domain.hello.jwt.shiro.system.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
/**
 * @description:
 * @author: domain
 * @create: 2019/8/18 13:43
 */

@RestController
public class WebController {

    private static final Logger LOGGER = LogManager.getLogger(WebController.class);

    private UserService userService;


    @Autowired
    public void setService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public ResponseBean login(@RequestParam("username") String username,
                              @RequestParam("password") String password) {
        UserBean userBean = userService.getUser(username);
        if (userBean.getPassword().equals(password)) {
            return new ResponseBean(200, "Login success", JWTUtil.sign(username));
        } else {
            throw new BusinessException("用户名或者密码错误");
        }
    }

    @GetMapping("/article")
    public ResponseBean article() {
            return new ResponseBean(200, "welcome to article", null);
    }

    @GetMapping("/records")
    public ResponseBean records() {
        return new ResponseBean(200, "welcome to records", null);
    }

    @GetMapping("/**")
    public ResponseBean index() {
            return new ResponseBean(200, "hello JWT and Shiro", null);
    }

}
