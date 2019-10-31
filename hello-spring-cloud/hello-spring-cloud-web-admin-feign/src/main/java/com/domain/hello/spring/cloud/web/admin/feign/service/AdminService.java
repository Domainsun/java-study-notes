package com.domain.hello.spring.cloud.web.admin.feign.service;

import com.domain.hello.spring.cloud.web.admin.feign.hystrix.WebAdminHystrix;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;



@FeignClient(value = "hello-spring-cloud-service-admin",fallback = WebAdminHystrix.class)
public interface AdminService {

    @RequestMapping(value = "hi",method = RequestMethod.GET)
    String sayHi(@RequestParam(value = "message") String message);

}

