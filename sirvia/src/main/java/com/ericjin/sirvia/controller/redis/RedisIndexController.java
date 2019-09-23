package com.ericjin.sirvia.controller.redis;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/redis")
public class RedisIndexController {
    @GetMapping("/index")
    public String index() {
        return "redis/index";
    }
}
