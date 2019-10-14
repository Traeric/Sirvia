package com.jinxin;

import com.ericjin.javadmin.Register;
import com.jinxin.beans.Article;
import com.jinxin.beans.Tags;
import com.jinxin.beans.Test;

import java.util.List;
import java.util.Map;

public class MyRegister extends Register {
    @Override
    public List<Class> userTableList() {
        List<Class> list = super.userTableList();
        list.add(Article.class);
        list.add(Tags.class);
        list.add(Test.class);
        return list;
    }

    @Override
    public Map<String, String> actionMap() {
        Map<String, String> stringStringMap = super.actionMap();
        stringStringMap.put("getNum", "统计数量");
        return stringStringMap;
    }
}
