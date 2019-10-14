package com.ericjin.sirvia.controller;

import com.ericjin.sirvia.service.RedisIndexService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;
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
        model.addAttribute("keys", redisKeys);
        return "redis/view_data";
    }

    @GetMapping("/opt_data")
    public String optData() {
        return "redis/opt_data";
    }

    @ResponseBody
    @PostMapping("/get_data")
    public Map<String, Object> getData(String key) {
        Map<String, Object> res = new LinkedHashMap<>();
        String data = redisIndexService.getData(key);
        res.put("flag", true);
        res.put("content", data);
        return res;
    }
}
