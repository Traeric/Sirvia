package com.ericjin.sirvia.shiro.factory;

import java.util.LinkedHashMap;

public class filterChainDefinitionMapBuilder {
    public LinkedHashMap<String, String> buildFilterChainDefinitionMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("/redis/login", "anon");
        map.put("/static/**", "anon");
        map.put("/redis/logout", "logout");
        map.put("/**", "authc");
        return map;
    }
}
