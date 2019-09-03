package com.ericjin.javadmin;

import com.ericjin.javadmin.mapper.SuperMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Transactional
public class Action {
    @Autowired
    private SuperMapper superMapper;

    /**
     * 删除选中的数据
     *
     * @param selectArr
     * @param tableName
     * @return
     */
    public Boolean deleteSelectData(List<Integer> selectArr, String tableName) {
        System.out.println(superMapper);
        AtomicReference<Boolean> flag = new AtomicReference<>(true);
        selectArr.parallelStream().forEach(id -> flag.set(superMapper.deleteTable(tableName, id)));
        return flag.get();
    }


}
