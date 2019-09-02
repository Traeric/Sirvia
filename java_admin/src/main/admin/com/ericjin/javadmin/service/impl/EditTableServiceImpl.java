package com.ericjin.javadmin.service.impl;

import com.ericjin.javadmin.annotation.Id;
import com.ericjin.javadmin.mapper.SuperMapper;
import com.ericjin.javadmin.service.EditTableService;
import com.ericjin.javadmin.service.IndexService;
import com.ericjin.javadmin.utils.FieldToInputStr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.lang.reflect.Field;
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
    public String getOneData(String modelName, String beanName, Integer id) {
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
                result.append(FieldToInputStr.getInputStrWithValue(field, map));
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
    public Boolean updateTable(String modelName, String beanName, Integer id, Map<String, Object> map) {
        // 获取javabean
        Class bean = indexService.getBean(modelName, beanName);
        // 获取表名
        String tableName = indexService.getTableName(bean);
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
    public Boolean deleteTable(String modelName, String beanName, Integer id) {
        // 获取java bean
        Class bean = indexService.getBean(modelName, beanName);
        // 获取表名
        String tableName = indexService.getTableName(bean);
        // 删除数据
        return superMapper.deleteTable(tableName, id);
    }
}
