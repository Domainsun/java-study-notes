package com.domain.hello.aop.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: domain
 * @create: 2019/9/14 14:35
 */

@RestController
public class WebController {

    @GetMapping("add")
    public String add(@RequestParam("userType") String userType){
        System.out.println("add：添加用户...");
        return "ok";
    }

    @GetMapping(value = "del")
    public String del(@RequestParam("userType") String userType){
        System.out.println("add：删除用户...");
        return "ok";
    }

}
