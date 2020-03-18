package com.hds.hystrix.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {
    @RequestMapping("/fallback")
    public String fallback() {
        return "请求超时!!!";
    }
}
