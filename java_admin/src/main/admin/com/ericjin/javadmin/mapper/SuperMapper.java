package com.ericjin.javadmin.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SuperMapper {
    // 查询表的所有信息
    List<Map<String, Object>> getAll(@Param("table_name") String tableName);

    // 添加一条数据到表
    Boolean addTable(@Param("data_map") Map<String, String> map, @Param("table_name") String tableName);

    // 查询一条数据
    Map<String, Object> getOneRecord(@Param("table_name") String tableName, @Param("id") Integer id);

    // 更新表中数据
    Boolean updateTable(@Param("table_name") String tableName, @Param("id") Integer id,
                        @Param("data_map") Map<String, Object> map);

    // 删除表数据
    Boolean deleteTable(@Param("table_name") String tableName, @Param("id") Integer id);

    // 获取外键关联的表信息
    List<Map<String, Object>> getForeignInfo(@Param("table_name") String tableName, @Param("relation_key") String relationKey,
                                       @Param("show_field") String showField);
}
