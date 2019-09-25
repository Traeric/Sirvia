package com.ericjin.sirvia.service.impl;

import com.ericjin.sirvia.service.RedisIndexService;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.Resource;
import java.util.Set;

@Service("redisIndexServiceImpl")
public class RedisIndexServiceImpl implements RedisIndexService {
    @Resource(name = "jedis")
    private Jedis jedis;

    @Override
    public Set<String> getRedisKeys() {
        return jedis.keys("*");
    }
}
