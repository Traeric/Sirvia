package com.ericjin.sirvia.service.impl;

import com.ericjin.sirvia.annotation.*;
import com.ericjin.sirvia.mapper.SuperMapper;
import com.ericjin.sirvia.service.IndexService;
import com.ericjin.sirvia.utils.FieldToInputStr;
import com.ericjin.sirvia.utils.ToCamelCase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;
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
     * @param modelName 模型名
     * @param tableName 表名
     * @return 返回所有的数据
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
     * 获取用户自定义的显示在sirvia的名称
     *
     * @param modelName 模型名
     * @param beanName javabean名
     * @return 用户自定义的表名
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
     * @param modelName 模型名
     * @param beanName javabean名
     * @return javabean名称
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
     * @param bean javabean名称
     * @return 表名
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
     * 获取关联表的信息
     *
     * @param field 字段名
     * @return 关联表信息
     */
    public List<Map<String, Object>> getRelationTableInfo(Field field) {
        // 获取表名，要展示的字段名，以及外键关联的字段名
        ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
        String tableName = foreignKey.relation_table();
        String relationField = foreignKey.relation_key();
        String showField = foreignKey.show_field();
        // 获取关联表的信息
        return superMapper.getForeignInfo(tableName, relationField, showField);
    }

    /**
     * 获取第三张表的信息
     *
     * @param field 字段名
     * @return 第三张表名
     */
    public List<Map<String, Object>> getThirdTableInfo(Field field) {
        // 获取注解
        ManyToManyField manyToManyField = field.getAnnotation(ManyToManyField.class);
        // 获取注解信息
        String relationTable = manyToManyField.relation_table();
        String relationField = manyToManyField.relation_field();
        String showField = manyToManyField.show_field();
        return superMapper.getForeignInfo(relationTable, relationField, showField);
    }

    /**
     * 获取表单展示的内容
     *
     * @param modelName 模型名
     * @param beanName javabean名
     * @return 表单展示名
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
                    // 获取关联表的信息
                    List<Map<String, Object>> foreignInfo = this.getRelationTableInfo(field);
                    result.append(FieldToInputStr.getInputStr(field, foreignInfo, modelName,
                            field.getAnnotation(ForeignKey.class).relation_bean().getSimpleName()));
                } else if (field.isAnnotationPresent(ManyToManyField.class)) {
                    List<Map<String, Object>> foreignInfo = this.getThirdTableInfo(field);
                    result.append(FieldToInputStr.getInputStr(field, foreignInfo, modelName,
                            field.getAnnotation(ManyToManyField.class).relation_bean().getSimpleName()));
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
     * @param map 用户填写的数据
     * @return 返回添加是否成功
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

        List<Map<String, String>> thirdInfo = new ArrayList<>();
        // 获取在第三张表中添加的数据
        Field[] declaredFields = bean.getDeclaredFields();
        // 获取关联表要插入第三章表的数据
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(ManyToManyField.class)) {
                // 获取注解
                ManyToManyField manyToManyField = field.getAnnotation(ManyToManyField.class);
                // 获取第三张表数据
                String thirdTable = manyToManyField.third_table();
                String thirdRelationField = manyToManyField.third_relation_field();
                String thirdSelfField = manyToManyField.third_self_field();
                // 获取要添加的字段
                String relationValues = (String) map.get(String.format("%s_%s", thirdTable, field.getName()));
                data_map.remove(String.format("%s_%s", thirdTable, field.getName()));
                // 获取当前表要插入第三张表的字段
                String insertField = manyToManyField.insert_field();
                // 将信息保存
                Map<String, String> m = new LinkedHashMap<>();
                m.put("third_table", thirdTable);
                m.put("third_relation_field", thirdRelationField);
                m.put("third_self_field", thirdSelfField);
                m.put("relation_values", relationValues);
                m.put("insert_field", insertField);
                thirdInfo.add(m);
            }
        }
        // 添加数据
        superMapper.addTable(data_map, tableName);
        String id = superMapper.getId();
        // 添加第三张表的数据
        thirdInfo.parallelStream().forEach(item -> {
            // 查询出当前表要在第三张表中插入的值
            String selfId = superMapper.manyToManySelfId(tableName, item.get("insert_field"), id);
            // 获取关联表中的所有要插入的值
            String values = item.get("relation_values");
            String[] valuesArr = values.trim().split(" ");
            // 将数据插入到第三张表中
            for (String value : valuesArr) {
                if (!"".equals(value)) {
                    superMapper.thirdInsert(item.get("third_table"), item.get("third_relation_field"), value,
                            item.get("third_self_field"), selfId);
                }
            }
        });
        return true;
    }

    /**
     * 获取所有的表信息
     *
     * @return 获取数据库中所有表的信息
     */
    @Override
    public List<String> getAllTables() {
        return superMapper.getAllTables();
    }

    /**
     * 执行sql语句
     * @param sql 要执行的sql语句
     */
    @Override
    public void executeSql(String sql) {
        superMapper.executeSql(sql);
    }

    @Override
    public String addSingleInput(String modelName, String beanName, String fieldName) {
        // 获取bean
        Class bean = getBean(modelName, beanName);
        // 获取对应的字段
        try {
            Field field = bean.getDeclaredField(fieldName);
            if (field.isAnnotationPresent(ForeignKey.class)) {
                // 获取外键
                ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
                // 查询出对应的数据
                List<Map<String, Object>> list = getRelationTableInfo(field);
                StringBuilder result = new StringBuilder(String.format("<div class=\"layui-inline\">\n" +
                        "      <label class=\"layui-form-label\">%s:</label>\n" +
                        "      <div class=\"layui-input-inline\">\n" +
                        "        <select name=\"%s\" lay-verify=\"required\" lay-search=\"\">", fieldName, fieldName));
                list.forEach(cMap -> result.append(String.format("<option value=\"%s\">%s</option>",
                        cMap.get(foreignKey.relation_key()), cMap.get(foreignKey.show_field()))));
                return result.append(String.format("</select></div>" +
                        "<button type=\"button\" class=\"layui-btn layui-btn-xs layui-btn-warm\" " +
                        "style='margin-left: 10px;' title='添加%s' onclick='openWindow(\"/admin/%s/%s/add\", \"%s\", this)'>" +
                        "<i class=\"layui-icon layui-icon-add-1\"></i>" +
                        "</button></div><script>layui.use('form', function () {\n" +
                        "let form1 = layui.form;" +
                        "form1.render();\n" +
                        "});</script>", fieldName, modelName, foreignKey.relation_bean().getSimpleName(), fieldName)).toString();
            } else if (field.isAnnotationPresent(ManyToManyField.class)) {
                return  "";
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return "0";
    }
}
