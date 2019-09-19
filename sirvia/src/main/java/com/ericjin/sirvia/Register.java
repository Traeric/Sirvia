package com.ericjin.sirvia;

import com.ericjin.sirvia.beans.User;

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
        return new ArrayList<>();
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
