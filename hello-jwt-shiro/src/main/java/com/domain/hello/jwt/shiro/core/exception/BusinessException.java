package com.domain.hello.jwt.shiro.core.exception;

/**
 * @description:
 * @author: domain
 * @create: 2019/8/18 13:49
 */

public class BusinessException extends RuntimeException {
    public BusinessException(String msg) {
        super(msg);
    }

    public BusinessException() {
        super();
    }
}
