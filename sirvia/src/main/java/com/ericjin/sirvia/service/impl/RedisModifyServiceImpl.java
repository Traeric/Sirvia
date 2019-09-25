package com.ericjin.sirvia.service.impl;

import com.ericjin.sirvia.service.RedisModifyService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RedisModifyServiceImpl implements RedisModifyService {
    @Resource(name = "jedis")
    private Jedis jedis;

    @Override
    public String saveString(String key, String content) {
        return jedis.set(key, content);
    }

    @Override
    public String reloadKeyString(String key, String newKey) {
        // 检查新键是否存在
        if (!jedis.exists(newKey)) {
            return "OK".equals(jedis.rename(key, newKey)) ? "更新成功" : "更新失败";
        } else {
            return "键已存在";
        }
    }

    @Override
    public Long remove(String key) {
        return jedis.del(key);
    }

    @Override
    public void removeLine(String key, Long[] list) {
        Long llen = jedis.llen(key);
        List<String> restItem = new ArrayList<>();
        for (Long i = 0L; i < llen; i++) {
            if (!Arrays.asList(list).contains(i)) {
                // 获取对应的值
                String item = jedis.lindex(key, i);
                restItem.add(item);
            }
        }
        // 删除现在的key对应的内容
        jedis.del(key);
        // 新建list
        restItem.forEach(item -> jedis.rpush(key, item));
    }

    @Override
    public Long addLineList(String key, String content) {
        return jedis.lpush(key, content);
    }
}
