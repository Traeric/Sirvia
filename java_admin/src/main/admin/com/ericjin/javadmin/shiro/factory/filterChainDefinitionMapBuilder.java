package com.ericjin.javadmin.shiro.factory;

import java.util.LinkedHashMap;

public class filterChainDefinitionMapBuilder {
    public LinkedHashMap<String, String> buildFilterChainDefinitionMap() {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        map.put("/admin/login", "anon");
        map.put("/static/**", "anon");
        map.put("/admin/logout", "logout");
        map.put("/**", "authc");
        return map;
    }
}
