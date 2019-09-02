package com.ericjin.javadmin;

import com.ericjin.javadmin.beans.User;
import com.test.hah.Article;
import com.test.hah.Tags;
import com.test.hah.Test;

import java.util.ArrayList;
import java.util.List;

public class Register {
    /**
     * 系统表
     * @return
     */
    public List<Class> systemTableList() {
        List<Class> list = new ArrayList<>();
        list.add(User.class);
        return list;
    }

    public List<Class> userTableList() {
        List<Class> list = new ArrayList<>();
        // 此处添加用户表信息
        list.add(Test.class);
        list.add(Article.class);
        list.add(Tags.class);
        return list;
    }
}
