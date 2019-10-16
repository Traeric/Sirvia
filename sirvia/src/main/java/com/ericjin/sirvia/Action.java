package com.ericjin.sirvia;

import com.ericjin.sirvia.mapper.SuperMapper;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class Action {

    /**
     * 删除选中的数据
     *
     * @param selectArr 被选中的数据
     * @param tableName 表名
     * @return 是否操作成功
     */
    public Boolean deleteSelectData(List<String> selectArr, String tableName, SuperMapper superMapper) {
        AtomicReference<Boolean> flag = new AtomicReference<>(true);
        selectArr.parallelStream().forEach(id -> flag.set(superMapper.deleteTable(tableName, id)));
        return flag.get();
    }
}
