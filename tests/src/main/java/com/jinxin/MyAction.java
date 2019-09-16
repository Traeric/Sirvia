package com.jinxin;

import com.ericjin.javadmin.Action;
import com.ericjin.javadmin.mapper.SuperMapper;

import java.util.List;

public class MyAction extends Action {
    public Boolean getNum(List<Integer> selectArr, String tableName, SuperMapper superMapper) {
        System.out.println("一共选中了" + selectArr.size() + "条数据");
        selectArr.forEach(System.out::println);
        return true;
    }
}
