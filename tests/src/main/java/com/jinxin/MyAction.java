package com.jinxin;

import com.ericjin.javadmin.Action;
import com.ericjin.javadmin.mapper.SuperMapper;

import java.util.List;

public class MyAction extends Action {
    public Boolean getNum(List<Integer> selectArr, String tableName, SuperMapper superMapper) {
        System.out.println("=============" + selectArr.size());
        return true;
    }
}
