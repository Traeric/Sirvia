package com.ericjin.sirvia;

import com.ericjin.sirvia.beans.User;
import com.ericjin.sirvia.test.Article;
import com.ericjin.sirvia.test.Tags;
import com.ericjin.sirvia.test.Test;

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
//        list.add(Article.class);
//        list.add(Test.class);
//        list.add(Tags.class);
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
