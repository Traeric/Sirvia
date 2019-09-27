package com.ericjin.sirvia.service.impl;

import com.ericjin.sirvia.CommonsSetting;
import com.ericjin.sirvia.service.RedisModifyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Connection;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Protocol;

import javax.annotation.Resource;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class RedisModifyServiceImpl implements RedisModifyService {
    @Resource(name = "jedis")
    private Jedis jedis;

    @Autowired
    private CommonsSetting commonsSetting;

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

    @Override
    public void removeLineSet(String key, String[] list) {
        for (String item : list) {
            jedis.srem(key, item);
        }
    }

    @Override
    public void addLineSet(String key, String content) {
        jedis.sadd(key, content);
    }

    @Override
    public void removeLineZset(String key, String[] list) {
        for (String item : list) {
            jedis.zrem(key, item);
        }
    }

    @Override
    public void addLineZset(String key, String content, String score) {
        jedis.zadd(key, Double.parseDouble(score), content);
    }

    @Override
    public void removeLineHash(String key, String[] list) {
        for (String item : list) {
            jedis.hdel(key, item);
        }
    }

    @Override
    public void addLineHash(String key, String hashKey, String content) {
        jedis.hset(key, hashKey, content);
    }

    @Override
    public Object executeCmd(String cmd) {
        // 分解命令
        String[] cmds = cmd.split(" ");
        Protocol.Command currentCmd = null;
        // 获取要执行的命令
        for (Protocol.Command command : Protocol.Command.values()) {
            if (cmds[0].toUpperCase().equals(command.toString())) {
                currentCmd = command;
            }
        }
        // 返回值
        Object res;
        try (Connection connection = new Connection(commonsSetting.getRedisBase().get("host"))) {
            Method method = Connection.class.getDeclaredMethod("sendCommand", Protocol.Command.class, String[].class);
            method.setAccessible(true);
            method.invoke(connection, currentCmd, cmd.split(" ", 2)[1].split(" "));
            res = connection.getOne();
        } catch (Exception e) {
            res = e.getMessage();
        }
        return res;
    }
}
