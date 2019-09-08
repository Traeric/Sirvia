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

    // 获取最近插入的id
    String getId();

    // 根据添加的id查询要在第三张表中插入的数据
    String manyToManySelfId(@Param("table_name") String tableName, @Param("select_field") String selectField, @Param("id") String id);

    // 多对多时将数据插入第三张表
    Boolean thirdInsert(@Param("third_table") String thirdTable, @Param("relation_field") String relationField,
                        @Param("relation_val") String relationVal, @Param("self_field") String selfField, @Param("self_val") String selfVal);

    // 查询某个值在第三张表中的全部结果
    List<Map<String, String>> getThirdInfo(@Param("third_table") String thirdTable, @Param("relation_field") String relationField,
                                           @Param("self_field") String selfField, @Param("select_value") String selectVal);

    // 从以三张表中移除数据
    Boolean removeFromThirdTable(@Param("third_table") String thirdTable, @Param("third_self_field") String thirdSelfField,
                                 @Param("select_id") String selectId);

    // 获取数据库中所有的表信息
    List<String> getAllTables();

    // 执行SQL语句
    void executeSql(@Param("sql") String sql);
}
