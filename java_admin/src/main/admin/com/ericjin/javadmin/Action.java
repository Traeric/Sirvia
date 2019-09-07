package com.ericjin.javadmin;

import com.ericjin.javadmin.mapper.SuperMapper;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Action {

    /**
     * 删除选中的数据
     *
     * @param selectArr
     * @param tableName
     * @return
     */
    public Boolean deleteSelectData(List<Integer> selectArr, String tableName, SuperMapper superMapper) {
        AtomicReference<Boolean> flag = new AtomicReference<>(true);
        selectArr.parallelStream().forEach(id -> flag.set(superMapper.deleteTable(tableName, id)));
        return flag.get();
    }

    public Boolean printSelectedData(List<Integer> selectArr, String tableName, SuperMapper superMapper) {
        selectArr.forEach(System.out::println);
        return true;
    }
}
