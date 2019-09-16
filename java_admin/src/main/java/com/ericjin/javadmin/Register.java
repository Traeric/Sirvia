package com.ericjin.javadmin;

import com.ericjin.javadmin.beans.User;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Register {
    /**
     * 系统表
     *
     * @return
     */
    public List<Class> systemTableList() {
        List<Class> list = new ArrayList<>();
        list.add(User.class);
        return list;
    }

    /**
     * 用户表
     *
     * @return
     */
    public List<Class> userTableList() {
        List<Class> list = new ArrayList<>();
        // 此处添加用户表信息
        return list;
    }

    /**
     * 想要执行的action
     *
     * @return
     */
    public Map<String, String> actionMap() {
        Map<String, String> map = new LinkedHashMap<>();
        // 添加要执行的action
        map.put("deleteSelectData", "删除选中的数据");
        return map;
    }
}
