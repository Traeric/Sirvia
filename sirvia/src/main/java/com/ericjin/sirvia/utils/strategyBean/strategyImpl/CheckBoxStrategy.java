package com.ericjin.sirvia.utils.strategyBean.strategyImpl;

import com.ericjin.sirvia.utils.strategyBean.BeanStrategy;

import java.util.Map;

public class CheckBoxStrategy implements BeanStrategy {
    @Override
    public String getEmptyInput(Map<String, Object> map) {
        String fieldName = (String) map.get("fieldName");
        return String.format("<div class=\"layui-form-item\">\n" +
                "            <label class=\"layui-form-label\">%s:</label>\n" +
                "            <div class=\"layui-input-block\">\n" +
                "                <input type=\"checkbox\" name=\"%s\" title=\"%s\">\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, fieldName);
    }

    @Override
    public String getFullInput(Map<String, Object> map) {
        String fieldName = (String) map.get("fieldName");
        String val = (String) map.get("val");
        if (val.equals("0")) {
            return String.format("<div class=\"layui-form-item\">\n" +
                    "            <label class=\"layui-form-label\">%s:</label>\n" +
                    "            <div class=\"layui-input-block\">\n" +
                    "                <input type=\"checkbox\" name=\"%s\" title=\"%s\">\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, fieldName);
        } else {
            return String.format("<div class=\"layui-form-item\">\n" +
                    "            <label class=\"layui-form-label\">%s:</label>\n" +
                    "            <div class=\"layui-input-block\">\n" +
                    "                <input type=\"checkbox\" name=\"%s\" title=\"%s\" checked>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, fieldName);
        }
    }
}
