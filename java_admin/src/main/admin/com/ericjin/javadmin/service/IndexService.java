package com.ericjin.javadmin.service;

import java.util.List;
import java.util.Map;

public interface IndexService {
    List<Map<String, Object>> getSingleTable(String modelName, String tableName);

    String getShowName(String modelName, String beanName);

    String getInputList(String modelName, String beanName);

    Boolean addTable(Map<String, Object> map, String modelName, String beanName);

    Class getBean(String modelName, String beanName);

    String getTableName(Class bean);
}
