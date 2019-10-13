package com.ericjin.sirvia.utils;

import com.ericjin.sirvia.annotation.*;
import com.ericjin.sirvia.utils.strategyBean.BeanContext;
import com.ericjin.sirvia.utils.strategyBean.strategyImpl.*;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FieldToInputStr {
    /**
     * 通过bean字段生成表单的公共内容
     * @param type bean类
     * @param field 反射字段
     * @param strategyMap map
     * @param list 一对多填充的值
     * @param modelName 模型名
     * @param beanName  javabean类名
     * @return 上下文
     */
    private static BeanContext common(Class type, Field field, Map<String, Object> strategyMap,
                                      List<Map<String, Object>> list, String modelName, String beanName) {
        String fieldName = ToCamelCase.humpToLine(field.getName());
        // Context中心
        BeanContext beanContext = new BeanContext(new TextStrategy());
        // 封装数据
        strategyMap.put("field", field);
        strategyMap.put("fieldName", fieldName);
        strategyMap.put("list", list);
        strategyMap.put("modelName", modelName);
        strategyMap.put("beanName", beanName);
        if (type == Integer.class || type == int.class || type == String.class || type == Long.class || type == long.class
                || type == List.class) {
            // 查看该字段上面是否标注了注解
            if (field.isAnnotationPresent(Choose.class)) {
                beanContext.setBeanStrategy(new ChooseStrategy());
            } else if (field.isAnnotationPresent(DateUse.class)) {
                beanContext.setBeanStrategy(new DateUseStrategy());
            } else if (field.isAnnotationPresent(Password.class)) {
                beanContext.setBeanStrategy(new PasswordStrategy());
            } else if (field.isAnnotationPresent(ForeignKey.class)) {
                beanContext.setBeanStrategy(new ForeignKeyStrategy());
            } else if (field.isAnnotationPresent(ManyToManyField.class)) {
                beanContext.setBeanStrategy(new ManyToManyFieldStrategy());
            } else {
                beanContext.setBeanStrategy(new TextStrategy());
            }
        } else if (type == Boolean.class || type == boolean.class) {
            beanContext.setBeanStrategy(new CheckBoxStrategy());
        } else if (type == Date.class) {
            beanContext.setBeanStrategy(new DateUseStrategy());
        } else if (type == StringBuffer.class || type == StringBuilder.class) {
            beanContext.setBeanStrategy(new TextAreaStrategy());
        }
        return beanContext;
    }

    /**
     * 通过字段类型获取对应的表单
     *
     * @param field     反射字段
     * @param list      填充的值
     * @param modelName 模型名
     * @param beanName  javabean名
     * @return 返回HTML表单
     */
    public static String getInputStr(Field field, List<Map<String, Object>> list, String modelName, String beanName) {
        Class type = field.getType();

        Map<String, Object> strategyMap = new HashMap<>();
        BeanContext beanContext = FieldToInputStr.common(type, field, strategyMap, list, modelName, beanName);
        return beanContext.getEmptyInput(strategyMap);
    }

    /**
     * 重载getInputStr
     *
     * @param field 反射字段
     * @return 返回HTML表单
     */
    public static String getInputStr(Field field) {
        return FieldToInputStr.getInputStr(field, null, "", "");
    }

    /**
     * 通过字段类型确定表单，并且填值
     * @param field 反射字段
     * @param map 多对多内容
     * @param list 一对多内容
     * @param selectData 多对多选中内容
     * @param modelName 模型名
     * @param beanName javabean类名
     * @return 返回带值的HTML表单
     */
    public static String getInputStrWithValue(Field field, Map<String, Object> map, List<Map<String, Object>> list,
                                              List<Map<String, String>> selectData, String modelName, String beanName) {
        Class type = field.getType();
        String fieldName = ToCamelCase.humpToLine(field.getName());
        // 由于是修改，需要获取已经有的值
        String val = String.valueOf(map.get(fieldName));
        // Context中心
        Map<String, Object> strategyMap = new HashMap<>();
        // 封装数据
        strategyMap.put("selectData", selectData);
        strategyMap.put("val", val);
        BeanContext beanContext = FieldToInputStr.common(type, field, strategyMap, list, modelName, beanName);
        return beanContext.getFullInput(strategyMap);
    }

    public static String getInputStrWithValue(Field field, Map<String, Object> map) {
        return FieldToInputStr.getInputStrWithValue(field, map, null);
    }

    private static String getInputStrWithValue(Field field, Map<String, Object> map, List<Map<String, Object>> list) {
        return FieldToInputStr.getInputStrWithValue(field, map, list, null, "", "");
    }
}
