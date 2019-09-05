package com.ericjin.javadmin.service.impl;

import com.ericjin.javadmin.annotation.EntityTableName;
import com.ericjin.javadmin.annotation.ForeignKey;
import com.ericjin.javadmin.annotation.Id;
import com.ericjin.javadmin.annotation.ShowName;
import com.ericjin.javadmin.mapper.SuperMapper;
import com.ericjin.javadmin.service.IndexService;
import com.ericjin.javadmin.utils.FieldToInputStr;
import com.ericjin.javadmin.utils.ToCamelCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("indexService")
@Transactional
public class IndexServiceImpl implements IndexService {
    @Resource(name = "systemTableList")
    private List<Class> systemTableList;

    @Resource(name = "userTableList")
    private List<Class> userTableList;

    @Autowired
    private SuperMapper superMapper;

    /**
     * 查询某张表的所有数据
     *
     * @param modelName
     * @param tableName
     * @return
     */
    @Override
    public List<Map<String, Object>> getSingleTable(String modelName, String tableName) {
        // 获取对应的javabean
        Class bean = this.getBean(modelName, tableName);
        // 获取实体类对应的表名
        String entityTableName = this.getTableName(bean);
        // 查询表数据
        return superMapper.getAll(entityTableName);
    }

    /**
     * 获取用户自定义的显示在java admin的名称
     *
     * @param modelName
     * @param beanName
     * @return
     */
    @Override
    public String getShowName(String modelName, String beanName) {
        Class bean = this.getBean(modelName, beanName);
        if (bean.isAnnotationPresent(ShowName.class)) {
            return ((ShowName) bean.getAnnotation(ShowName.class)).name();
        }
        return bean.getSimpleName();
    }

    /**
     * 获取对应的javabean
     *
     * @param modelName
     * @param beanName
     * @return
     */
    public Class getBean(String modelName, String beanName) {
        List<Class> tableList = new ArrayList<>();
        if ("system".equals(modelName)) {
            tableList = systemTableList;
        } else if ("user".equals(modelName)) {
            tableList = userTableList;
        }
        return tableList.parallelStream().filter((table) -> beanName.equals(table.getSimpleName()))
                .collect(Collectors.toList()).get(0);
    }

    /**
     * 根据对象获取表名
     *
     * @param bean
     * @return
     */
    public String getTableName(Class bean) {
        if (bean.isAnnotationPresent(EntityTableName.class)) {
            // 获取注解
            EntityTableName entityTableNameAnno = (EntityTableName) bean.getAnnotation(EntityTableName.class);
            // 获取表名
            return entityTableNameAnno.name();
        }
        return ToCamelCase.humpToLine(bean.getSimpleName());
    }

    /**
     * 获取表单展示的内容
     *
     * @param modelName
     * @param beanName
     * @return
     */
    @Override
    public String getInputList(String modelName, String beanName) {
        // 获取javabean
        Class bean = this.getBean(modelName, beanName);
        // 获取所有的字段
        Field[] declaredFields = bean.getDeclaredFields();
        StringBuilder result = new StringBuilder();
        for (Field field : declaredFields) {
            // 排除id
            if (!field.isAnnotationPresent(Id.class)) {
                // 处理外键
                if (field.isAnnotationPresent(ForeignKey.class)) {
                    // 获取表名，要展示的字段名，以及外键关联的字段名
                    ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
                    String tableName = foreignKey.relation_table();
                    String relationField = foreignKey.relation_key();
                    String showField = foreignKey.show_field();
                    // 获取关联表的信息
                    List<Map<String, Object>> foreignInfo = superMapper.getForeignInfo(tableName, relationField, showField);
                    result.append(FieldToInputStr.getInputStr(field, foreignInfo));
                } else {
                    result.append(FieldToInputStr.getInputStr(field));
                }
            }
        }
        return result.toString();
    }

    /**
     * 添加数据到表
     *
     * @param map
     * @return
     */
    @Override
    public Boolean addTable(Map<String, Object> map, String modelName, String beanName) {
        // 获取表名
        // 获取javabean对象
        Class bean = this.getBean(modelName, beanName);
        // 获取表名
        String tableName = this.getTableName(bean);
        // 整理数据
        Map<String, String> data_map = new HashMap<>();
        map.forEach((key, val) -> data_map.put(ToCamelCase.humpToLine(key), (String) val));
        // 添加数据
        return superMapper.addTable(data_map, tableName);
    }
}
