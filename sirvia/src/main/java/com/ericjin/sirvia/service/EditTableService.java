package com.ericjin.sirvia.service;

import java.util.Map;

public interface EditTableService {
    String getOneData(String modelName, String beanName, Integer id);

    Boolean updateTable(String modelName, String beanName, Integer id, Map<String, Object> map);

    Boolean deleteTable(String modelName, String beanName, Integer id);

    String getDeleteRelationInfo(String modelName, String beanName, Integer id);
}
