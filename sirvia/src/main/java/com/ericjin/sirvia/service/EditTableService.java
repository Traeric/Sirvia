package com.ericjin.sirvia.service;

import java.util.Map;

public interface EditTableService {
    String getOneData(String modelName, String beanName, String id);

    Boolean updateTable(String modelName, String beanName, String id, Map<String, Object> map);

    Boolean deleteTable(String modelName, String beanName, String id);

    String getDeleteRelationInfo(String modelName, String beanName, String id);

    String editSingleInput(String modelName, String beanName, String fieldName, String id);
}
