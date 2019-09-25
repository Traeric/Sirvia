package com.ericjin.sirvia.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisConfig {
    @Value("#{commonsSetting.redisBase.get('host')}")
    private String host;

    @Value("#{commonsSetting.redisBase.get('port')}")
    private Integer port;

    @Value("#{commonsSetting.redisBase.get('passowrd')}")
    private String password;

    @Value("#{commonsSetting.redisBase.get('database')}")
    private Integer dbs;


    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxTotal(200);
        jedisPoolConfig.setMaxIdle(50);
        jedisPoolConfig.setTestOnBorrow(true);
        jedisPoolConfig.setTestOnReturn(true);
        return jedisPoolConfig;
    }

    @Bean
    public JedisPool jedisPool() {
        return new JedisPool(jedisPoolConfig(), host, port, 30000, password, dbs);
    }
}
