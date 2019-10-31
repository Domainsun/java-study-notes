package com.domain.hello.spring.cloud.pos.feign;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @description:
 * @author: domain
 * @create: 2019/8/11 10:46
 */

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EnableHystrixDashboard
public class PosFeginApplication {
    public static void main(String[] args) {
        SpringApplication.run(PosFeginApplication.class,args);
    }
}
