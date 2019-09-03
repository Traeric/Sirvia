package com.ericjin.javadmin.service;

import java.util.List;

public interface TableListService {
    Boolean doAction(List<Integer> selectArr, String methodName, String modelName, String beanName);
}
