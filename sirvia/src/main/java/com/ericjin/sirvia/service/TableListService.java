package com.ericjin.sirvia.service;

import java.util.List;

public interface TableListService {
    Boolean doAction(List<String> selectArr, String methodName, String modelName, String beanName);
}
