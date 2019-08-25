package com.domain.hello.rabbitmq.controller;

import com.domain.hello.rabbitmq.domain.Bank;
import com.domain.hello.rabbitmq.domain.User;
import com.domain.hello.rabbitmq.provider.SyncProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @description:
 * @author: domain
 * @create: 2019/8/24 15:40
 */

@RestController
public class WebAdminController {


    @Autowired
    SyncProvider syncProvider;

    @GetMapping("addUser")
    public String addUser(String name,Integer age){

        //添加到消息队列
        for (int i = 0; i < 100; i++) {
            User user = new User(name, i);
            syncProvider.sendUser(user);
        }

        return String.format("add user %s successful.",name);
    }

    @GetMapping("addBank")
    public String addBank (String name,String code ){
        Bank bank = new Bank(name, code);
        //添加到消息队列
        syncProvider.sendBank(bank);
        return String.format("add bank %s successful.",name);
    }

}
