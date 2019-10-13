package com.ericjin.sirvia.utils.strategyBean;

import java.util.Map;

public interface BeanStrategy {
    String getEmptyInput(Map<String, Object> map);

    String getFullInput(Map<String, Object> map);
}
