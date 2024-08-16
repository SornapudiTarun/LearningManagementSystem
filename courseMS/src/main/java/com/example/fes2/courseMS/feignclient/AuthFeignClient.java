package com.example.fes2.courseMS.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "AUTH-MS" , url = "${coursems.feign.url.auththenticationms}")
public interface AuthFeignClient {

    @GetMapping("company/isValidToken")
    boolean isValidToken(@RequestHeader("Authorization") String token);

    @GetMapping("company/getRole")
    String getRole(@RequestHeader("Authorization") String token);
}
