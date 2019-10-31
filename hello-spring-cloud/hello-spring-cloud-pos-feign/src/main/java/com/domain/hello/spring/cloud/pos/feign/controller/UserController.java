package com.domain.hello.spring.cloud.pos.feign.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description: 用户视图控制器
 * @author: domain
 * @create: 2019/8/11 10:53
 */

@RestController
public class UserController {

    @GetMapping("/login")
    public String login(String userName){
        return String.format("welcome %s",userName);
    }

}
