package com.ericjin.sirvia.utils.strategyBean;

import java.util.Map;

/**
 * 负责和具体的策略类交互
 */
public class BeanContext {
    private BeanStrategy beanStrategy;

    public BeanContext(BeanStrategy beanStrategy) {
        this.beanStrategy = beanStrategy;
    }

    public void setBeanStrategy(BeanStrategy beanStrategy) {
        this.beanStrategy = beanStrategy;
    }

    public String getEmptyInput(Map<String, Object> map) {
        return beanStrategy.getEmptyInput(map);
    }

    public String getFullInput(Map<String, Object> map) {
        return beanStrategy.getFullInput(map);
    }
}
