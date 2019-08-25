package com.domain.hello.rabbitmq.domain;


import java.io.Serializable;

/**
 * @description:
 * @author: domain
 * @create: 2019/8/24 16:08
 */

public class Bank implements Serializable{


    //银行名称
    private String name;

    //银行编码
    private String code;


    public Bank(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}
