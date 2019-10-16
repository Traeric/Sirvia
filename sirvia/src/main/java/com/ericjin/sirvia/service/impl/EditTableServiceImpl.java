package com.ericjin.sirvia.service.impl;

import com.ericjin.sirvia.annotation.ForeignKey;
import com.ericjin.sirvia.annotation.Id;
import com.ericjin.sirvia.annotation.ManyToManyField;
import com.ericjin.sirvia.annotation.OneField;
import com.ericjin.sirvia.mapper.SuperMapper;
import com.ericjin.sirvia.service.EditTableService;
import com.ericjin.sirvia.service.IndexService;
import com.ericjin.sirvia.utils.FieldToInputStr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@Service("editTableService")
@Transactional
public class EditTableServiceImpl implements EditTableService {
    @Resource(name = "indexService")
    private IndexService indexService;

    @Autowired
    private SuperMapper superMapper;


    /**
     * 获取表单数据
     *
     * @return
     */
    public String getOneData(String modelName, String beanName, String id) {
        // 获取javabean
        Class bean = indexService.getBean(modelName, beanName);
        // 获取数据库中的数据
        String tableName = indexService.getTableName(bean);
        Map<String, Object> map = superMapper.getOneRecord(tableName, id);
        // 获取所有的字段
        Field[] declaredFields = bean.getDeclaredFields();
        StringBuilder result = new StringBuilder();
        for (Field field : declaredFields) {
            // 排除id
            if (!field.isAnnotationPresent(Id.class)) {
                // 处理外键
                if (field.isAnnotationPresent(ForeignKey.class)) {
                    // 获取关联表的信息
                    List<Map<String, Object>> foreignInfo = indexService.getRelationTableInfo(field);
                    result.append(FieldToInputStr.getInputStrWithValue(field, map, foreignInfo, null, modelName,
                            field.getAnnotation(ForeignKey.class).relation_bean().getSimpleName()));
                } else if (field.isAnnotationPresent(ManyToManyField.class)) {
                    // 获取注解
                    ManyToManyField manyToManyField = field.getAnnotation(ManyToManyField.class);
                    // 获取注解信息
                    String relationTable = manyToManyField.relation_table();
                    String relationField = manyToManyField.relation_field();
                    String showField = manyToManyField.show_field();
                    List<Map<String, Object>> foreignInfo = superMapper.getForeignInfo(relationTable, relationField, showField);
                    // 获取第三张表的信息
                    String thirdTable = manyToManyField.third_table();
                    String thirdRelationField = manyToManyField.third_relation_field();
                    String thirdSelfField = manyToManyField.third_self_field();
                    String insertField = manyToManyField.insert_field();
                    String selectVal = superMapper.manyToManySelfId(tableName, insertField, String.valueOf(id));
                    // 查询第三张表的数据
                    List<Map<String, String>> thirdInfo = superMapper.getThirdInfo(thirdTable, thirdRelationField, thirdSelfField, selectVal);
                    result.append(FieldToInputStr.getInputStrWithValue(field, map, foreignInfo, thirdInfo, modelName,
                            manyToManyField.relation_bean().getSimpleName()));
                } else {
                    result.append(FieldToInputStr.getInputStrWithValue(field, map));
                }
            }
        }
        return result.toString();
    }


    /**
     * 更新数据表
     *
     * @param modelName
     * @param beanName
     * @param id
     * @return
     */
    @Override
    public Boolean updateTable(String modelName, String beanName, String id, Map<String, Object> map) {
        // 获取javabean
        Class bean = indexService.getBean(modelName, beanName);
        // 获取表名
        String tableName = indexService.getTableName(bean);
        // 移除不必要的数据
        map.remove("layTransferLeftCheckAll");
        map.remove("layTransferLeftCheck");
        map.remove("layTransferRightCheck");
        map.remove("layTransferRightCheckAll");
        // 处理多对多的数据
        Field[] declaredFields = bean.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(ManyToManyField.class)) {
                // 获取注解
                ManyToManyField manyToManyField = field.getAnnotation(ManyToManyField.class);
                // 获取第三张表的信息
                String thirdTable = manyToManyField.third_table();
                String thirdRelationField = manyToManyField.third_relation_field();
                String thirdSelfField = manyToManyField.third_self_field();
                String insertField = manyToManyField.insert_field();
                // 根据id查询出要当前表要在第三张表中插入的内容
                String insertId = superMapper.manyToManySelfId(tableName, insertField, String.valueOf(id));
                // 先删除第三张表中thirdSelfField=insertId的数据
                superMapper.removeFromThirdTable(thirdTable, thirdSelfField, insertId);
                // 获取要更新的数据
                String[] valueArr = String.valueOf(map.get(String.format("%s_%s", thirdTable, field.getName()))).trim().split(" ");
                map.remove(String.format("%s_%s", thirdTable, field.getName()));   // 从map中移除数据，避免后续更新的时候有脏数据
                for (String value : valueArr) {
                    if (!"".equals(value)) {
                        superMapper.thirdInsert(thirdTable, thirdRelationField, value, thirdSelfField, insertId);
                    }
                }
            }
        }
        // 更新数据
        return superMapper.updateTable(tableName, id, map);
    }

    /**
     * 删除表数据
     *
     * @param modelName
     * @param beanName
     * @param id
     * @return
     */
    @Override
    public Boolean deleteTable(String modelName, String beanName, String id) {
        // 获取java bean
        Class bean = indexService.getBean(modelName, beanName);
        // 获取表名
        String tableName = indexService.getTableName(bean);
        // 删除数据
        return superMapper.deleteTable(tableName, id);
    }

    @Override
    public String getDeleteRelationInfo(String modelName, String beanName, String id) {
        StringBuffer result = new StringBuffer();
        // 获取表名
        Class bean = indexService.getBean(modelName, beanName);
        // 获取所有的字段
        Field[] fields = bean.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(ManyToManyField.class)) {
                // 获取注解
                ManyToManyField manyToManyField = field.getAnnotation(ManyToManyField.class);
                // 获取第三张表的名字
                String thirdTableName = manyToManyField.third_table();
                // 需要当前表在第三张表中关联的字段
                String thirdSelfField = manyToManyField.third_self_field();
                // 获取第三章表中所有与当前删除字段相关的内容
                List<String> thirdDeleteInfo = superMapper.getThirdDeleteInfo(thirdTableName, thirdSelfField, id);
                if (thirdDeleteInfo.size() == 0) {
                    continue;
                }
                // 生成HTML信息
                result.append(String.format("<div class=\"layui-card\">\n" +
                        "                <div class=\"layui-card-header\">%s</div>\n" +
                        "                <div class=\"layui-card-body\">\n" +
                        "                    <ul class=\"layui-timeline\">", thirdTableName));
                // 生成列表信息
                thirdDeleteInfo.forEach(currentId -> result.append(String.format("<li class=\"layui-timeline-item\">\n" +
                        "                            <i class=\"layui-icon layui-timeline-axis\">&#xe756;</i>\n" +
                        "                            <div class=\"layui-timeline-content layui-text\">\n" +
                        "                                <p>如果你想要删除当前记录，那么%s的id为%s的记录也会被改变。</p>\n" +
                        "                            </div>\n" +
                        "                        </li>", thirdTableName, currentId)));
                result.append("</ul></div></div>");
            }

            if (field.isAnnotationPresent(OneField.class)) {
                // 获取注解
                OneField oneField = field.getAnnotation(OneField.class);
                // 获取关联表信息
                String relationTable = oneField.relation_table();
                // 获取外键名
                String foreignKey = oneField.foreign_key();
                // 获取关联表对应的javabean
                Class relationBean = oneField.relation_bean();
                // 查询出关联表中与之关联的数据
                List<String> relationDeleteInfo = superMapper.getRelationDeleteInfo(relationTable, foreignKey, id);
                if (relationDeleteInfo.size() == 0) {
                    continue;
                }
                // 生成HTML信息
                result.append(String.format("<div class=\"layui-card\">\n" +
                        "                <div class=\"layui-card-header\">%s</div>\n" +
                        "                <div class=\"layui-card-body\">\n" +
                        "                    <ul class=\"layui-timeline\">", relationTable));
                // 生成列表信息
                relationDeleteInfo.forEach(currentId -> result.append(String.format("<li class=\"layui-timeline-item\">\n" +
                        "                            <i class=\"layui-icon layui-timeline-axis\">&#xe756;</i>\n" +
                        "                            <div class=\"layui-timeline-content layui-text\">\n" +
                        "                                <p>如果你想要删除当前记录，那么<a\n" +
                        "                                        href=\"/admin/%s/%s/edit/%s\"\n" +
                        "                                        target=\"_blank\">%s的id为%s的记录</a>也会被改变。</p>\n" +
                        "                            </div>\n" +
                        "                        </li>", modelName, relationBean.getSimpleName(), currentId, relationTable, currentId)));
                result.append("</ul></div></div>");
            }
        }
        return result.toString();
    }
}
