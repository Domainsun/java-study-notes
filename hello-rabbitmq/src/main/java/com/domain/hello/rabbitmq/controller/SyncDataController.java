package com.domain.hello.rabbitmq.controller;

import com.domain.hello.rabbitmq.domain.Bank;
import com.domain.hello.rabbitmq.domain.BaseReponse;
import com.domain.hello.rabbitmq.domain.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * @description:
 * @author: domain
 * @create: 2019/8/24 15:47
 */

@RestController
public class SyncDataController {

    private int count=0;

    private int bankCount=0;

    @PostMapping("syncUser")
    public BaseReponse syncUser(@RequestBody User user) throws InterruptedException {
        System.out.println("同步用户数据");
        Thread.sleep(2000);
        ++count;
        if (user.getAge() % 2 == 0 && count % 10 != 0) {
            return new BaseReponse("40000", "同步失败");
        } else {
            return new BaseReponse("同步成功");
        }
    }

    @PostMapping("syncBank")
    public BaseReponse syncBank(@RequestBody Bank bank) throws InterruptedException {
        System.out.println("同步银行数据");
        Thread.sleep(2000);
        ++bankCount;
        if (Integer.valueOf(bank.getCode())%2==0 && bankCount%10!=0) {
            return new BaseReponse("40000", "同步失败");
        }else {
            return new BaseReponse("同步成功");
        }
    }

}
