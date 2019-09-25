package com.ericjin.sirvia.service;

import java.util.Set;

public interface RedisIndexService {
    Set<String> getRedisKeys();

    String getData(String key);
}
