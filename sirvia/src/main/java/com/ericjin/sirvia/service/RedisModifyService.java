package com.ericjin.sirvia.service;

import java.util.Objects;

public interface RedisModifyService {
    String saveString(String key, String content);

    String reloadKeyString(String key, String newKey);

    Long remove(String key);

    void removeLine(String key, Long[] list);

    Long addLineList(String key, String content);

    void removeLineSet(String key, String[] list);

    void addLineSet(String key, String content);

    void removeLineZset(String key, String[] list);

    void addLineZset(String key, String content, String score);

    void removeLineHash(String key, String[] list);

    void addLineHash(String key, String hashKey, String content);

    Object executeCmd(String cmd);

    void modifyList(String key, String value, Integer index);

    void modifySet(String key, String oldValue, String newValue);

    void modifyZset(String key, String oldValue, String newValue, String score);

    void modifyHash(String key, String oldField, String newField, String value);
}
