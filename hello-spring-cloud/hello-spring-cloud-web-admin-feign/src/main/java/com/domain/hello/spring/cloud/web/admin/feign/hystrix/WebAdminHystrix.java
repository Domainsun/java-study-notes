package com.domain.hello.spring.cloud.web.admin.feign.hystrix;

import com.domain.hello.spring.cloud.web.admin.feign.service.AdminService;
import org.springframework.stereotype.Component;


@Component
public class WebAdminHystrix implements AdminService {

    @Override
    public String sayHi(String message) {
        return "很抱歉,请求失败";
    }
}
