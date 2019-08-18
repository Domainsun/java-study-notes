package com.domain.hello.jwt.shiro.system.service;

import com.domain.hello.jwt.shiro.domain.UserBean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: domain
 * @create: 2019/8/18 14:06
 */

@Component
public class UserService {


    private final static Map<String ,String > userMap=new HashMap<String, String>(){
        {
            put("domain","domain123");
            put("alex","alex123");
        }
    };

    public UserBean getUser(String username) {
        // 没有此用户直接返回null
        if (! userMap.containsKey(username))
            return null;

        UserBean user = new UserBean();

        user.setUsername(username);
        user.setPassword(userMap.get(username));
        return user;
    }
}
