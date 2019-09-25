package com.ericjin.sirvia.service;

public interface RedisModifyService {
    String saveString(String key, String content);

    String reloadKeyString(String key, String newKey);

    Long remove(String key);

    void removeLine(String key, Long[] list);

    Long addLineList(String key, String content);
}
