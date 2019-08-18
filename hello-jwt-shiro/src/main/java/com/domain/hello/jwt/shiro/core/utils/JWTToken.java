package com.domain.hello.jwt.shiro.core.utils;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @description:
 * @author: domain
 * @create: 2019/8/18 13:52
 */

public class JWTToken implements AuthenticationToken {

    // 密钥
    private String token;

    public JWTToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}