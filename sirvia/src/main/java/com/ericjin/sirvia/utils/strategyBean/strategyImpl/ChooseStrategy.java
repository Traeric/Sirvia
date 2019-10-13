package com.ericjin.sirvia.utils.strategyBean.strategyImpl;

import com.ericjin.sirvia.Settings;
import com.ericjin.sirvia.annotation.Choose;
import com.ericjin.sirvia.utils.strategyBean.BeanStrategy;

import java.lang.reflect.Field;
import java.util.Map;

public class ChooseStrategy implements BeanStrategy {
    @Override
    public String getEmptyInput(Map<String, Object> map) {
        // 从map中解构数据
        Field field = (Field) map.get("field");
        String fieldName = (String) map.get("fieldName");

        // 获取注解
        Choose choose = field.getAnnotation(Choose.class);
        // 获取valueList跟textList
        String[] valueList = choose.valueList();
        String[] textList = choose.textList();
        StringBuilder result = null;
        if (choose.type() == Settings.RADIO) {
            result = new StringBuilder(String.format("<div class=\"layui-form-item\">\n" +
                    "    <label class=\"layui-form-label\">%s:</label>\n" +
                    "    <div class=\"layui-input-block\">", fieldName));
            // radio类型
            for (int i = 0; i < valueList.length; i++) {
                result.append(String.format("<input type=\"radio\" name=\"%s\" value=\"%s\" " +
                        "title=\"%s\">", fieldName, valueList[i], textList[i]));
            }
            result.append("</div></div><hr class=\"layui-bg-gray\">");
        } else if (choose.type() == Settings.SELECT) {
            // select类型
            result = new StringBuilder(String.format("<div class=\"layui-inline\">\n" +
                    "      <label class=\"layui-form-label\">%s:</label>\n" +
                    "      <div class=\"layui-input-inline\">\n" +
                    "        <select name=\"%s\" lay-verify=\"required\" lay-search=\"\">", fieldName, fieldName));
            for (int i = 0; i < valueList.length; i++) {
                result.append(String.format("<option value=\"%s\">%s</option>", valueList[i], textList[i]));
            }
            result.append("</select></div></div><hr class=\"layui-bg-gray\">");
        }
        return result == null ? "" : result.toString();
    }

    @Override
    public String getFullInput(Map<String, Object> map) {
        Field field = (Field) map.get("field");
        String fieldName = (String) map.get("fieldName");
        String val = (String) map.get("val");

        // 获取注解
        Choose choose = field.getAnnotation(Choose.class);
        // 获取valueList跟textList
        String[] valueList = choose.valueList();
        String[] textList = choose.textList();
        StringBuilder result = null;
        if (choose.type() == Settings.RADIO) {
            result = new StringBuilder(String.format("<div class=\"layui-form-item\">\n" +
                    "    <label class=\"layui-form-label\">%s:</label>\n" +
                    "    <div class=\"layui-input-block\">", fieldName));
            // radio类型
            for (int i = 0; i < valueList.length; i++) {
                if (valueList[i].equals(val)) {
                    result.append(String.format("<input type=\"radio\" name=\"%s\" value=\"%s\" " +
                            "title=\"%s\" checked>", fieldName, valueList[i], textList[i]));
                } else {
                    result.append(String.format("<input type=\"radio\" name=\"%s\" value=\"%s\" " +
                            "title=\"%s\">", fieldName, valueList[i], textList[i]));
                }
            }
            result.append("</div></div><hr class=\"layui-bg-gray\">");
        } else if (choose.type() == Settings.SELECT) {
            // select类型
            result = new StringBuilder(String.format("<div class=\"layui-inline\">\n" +
                    "      <label class=\"layui-form-label\">%s:</label>\n" +
                    "      <div class=\"layui-input-inline\">\n" +
                    "        <select name=\"%s\" lay-verify=\"required\" lay-search=\"\">", fieldName, fieldName));
            for (int i = 0; i < valueList.length; i++) {
                if (valueList[i].equals(val)) {
                    result.append(String.format("<option value=\"%s\" checked>%s</option>", valueList[i], textList[i]));
                } else {
                    result.append(String.format("<option value=\"%s\">%s</option>", valueList[i], textList[i]));
                }
            }
            result.append("</select></div></div><hr class=\"layui-bg-gray\">");
        }
        return result == null ? "" : result.toString();
    }
}
