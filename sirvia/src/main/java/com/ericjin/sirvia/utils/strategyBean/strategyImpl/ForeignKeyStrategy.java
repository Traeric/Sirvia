package com.ericjin.sirvia.utils.strategyBean.strategyImpl;

import com.ericjin.sirvia.annotation.ForeignKey;
import com.ericjin.sirvia.utils.strategyBean.BeanStrategy;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ForeignKeyStrategy implements BeanStrategy {
    @Override
    public String getEmptyInput(Map<String, Object> map) {
        // 解构数据
        Field field = (Field) map.get("field");
        String fieldName = (String) map.get("fieldName");
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
        String modelName = (String) map.get("modelName");
        String beanName = (String) map.get("beanName");
        // 获取键值对
        ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
        // select类型
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
                "</button></div><hr class=\"layui-bg-gray\">", fieldName, modelName, beanName, fieldName)).toString();
    }

    @Override
    public String getFullInput(Map<String, Object> map) {
        // 解构数据
        Field field = (Field) map.get("field");
        String fieldName = (String) map.get("fieldName");
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
        String beanName = (String) map.get("beanName");
        String modelName = (String) map.get("modelName");
        String val = (String) map.get("val");
        // 获取键值对
        ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
        // select类型
        StringBuilder result = new StringBuilder(String.format("<div class=\"layui-inline\">\n" +
                "      <label class=\"layui-form-label\">%s:</label>\n" +
                "      <div class=\"layui-input-inline\">\n" +
                "        <select name=\"%s\" lay-verify=\"required\" lay-search=\"\">", fieldName, fieldName));
        list.forEach(map1 -> {
            if (val.equals(String.valueOf(map1.get(foreignKey.relation_key())))) {
                result.append(String.format("<option value=\"%s\" selected = \"selected\">%s</option>",
                        map1.get(foreignKey.relation_key()), map1.get(foreignKey.show_field())));
            } else {
                result.append(String.format("<option value=\"%s\">%s</option>",
                        map1.get(foreignKey.relation_key()), map1.get(foreignKey.show_field())));
            }
        });
        return result.append(String.format("</select></div>" +
                "<button type='button' class='layui-btn layui-btn-xs layui-btn-warm' style='margin-left: 10px;' " +
                "title='添加%s' onclick='openWindow(\"/admin/%s/%s/add\", \"%s\", this)'>" +
                "<i class='layui-icon layui-icon-add-1'></i>" +
                "</button></div><hr class=\"layui-bg-gray\">", fieldName, modelName, beanName, fieldName)).toString();
    }
}
