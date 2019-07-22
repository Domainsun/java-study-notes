package com.domain.hello.mybatisplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.domain.hello.mybatisplus.mapper")
public class HelloMybatisplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(HelloMybatisplusApplication.class, args);
    }

}
