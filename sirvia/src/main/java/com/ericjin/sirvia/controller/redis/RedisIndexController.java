package com.ericjin.sirvia.controller.redis;

import com.ericjin.sirvia.service.RedisIndexService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.Set;

@Controller
@RequestMapping("/admin/redis")
public class RedisIndexController {
    @Resource(name = "redisIndexServiceImpl")
    private RedisIndexService redisIndexService;


    @GetMapping("/index")
    public String index() {
        return "redis/index";
    }

    @GetMapping("/view_data")
    public String viewData(Model model) {
        // 获取所有的key
        Set<String> redisKeys = redisIndexService.getRedisKeys();
        System.out.println(redisKeys);
        model.addAttribute("keys", redisKeys);
        return "redis/view_data";
    }

    @GetMapping("/opt_data")
    public String optData() {
        return "redis/opt_data";
    }
}
