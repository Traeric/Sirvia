package com.ericjin.sirvia.utils.strategyBean.strategyImpl;

import com.ericjin.sirvia.utils.strategyBean.BeanStrategy;

import java.util.Map;

public class DateUseStrategy implements BeanStrategy {
    @Override
    public String getEmptyInput(Map<String, Object> map) {
        String fieldName = (String) map.get("fieldName");
        return String.format("<div class=\"layui-inline\">\n" +
                "            <label class=\"layui-form-label\">%s:</label>\n" +
                "            <div class=\"layui-input-inline\">\n" +
                "                <input type=\"text\" name=\"%s\" id=\"date\" lay-verify=\"date\" placeholder=\"yyyy-MM-dd\" autocomplete=\"off\"\n" +
                "                       class=\"layui-input\">\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <hr class=\"layui-bg-gray\">", fieldName, fieldName);
    }

    @Override
    public String getFullInput(Map<String, Object> map) {
        String fieldName = (String) map.get("fieldName");
        String val = (String) map.get("val");
        return String.format("<div class=\"layui-inline\">\n" +
                "            <label class=\"layui-form-label\">%s:</label>\n" +
                "            <div class=\"layui-input-inline\">\n" +
                "                <input type=\"text\" name=\"%s\" id=\"date\" lay-verify=\"date\" placeholder=\"yyyy-MM-dd\" autocomplete=\"off\"\n" +
                "                       class=\"layui-input\" value='%s'>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, val);
    }
}
