package com.ericjin.sirvia.utils.strategyBean.strategyImpl;

import com.ericjin.sirvia.utils.strategyBean.BeanStrategy;

import java.util.Map;

public class TextAreaStrategy implements BeanStrategy {
    @Override
    public String getEmptyInput(Map<String, Object> map) {
        String fieldName = (String) map.get("fieldName");
        return String.format("<div class=\"layui-form-item layui-form-text\">\n" +
                "            <label class=\"layui-form-label\">%s:</label>\n" +
                "            <div class=\"layui-input-block\">\n" +
                "                <textarea name='%s' placeholder=\"请输入内容\" class=\"layui-textarea\"></textarea>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <hr class=\"layui-bg-gray\">", fieldName, fieldName);
    }

    @Override
    public String getFullInput(Map<String, Object> map) {
        String fieldName = (String) map.get("fieldName");
        String val = (String) map.get("val");
        return String.format("<div class=\"layui-form-item layui-form-text\">\n" +
                "            <label class=\"layui-form-label\">%s:</label>\n" +
                "            <div class=\"layui-input-block\">\n" +
                "                <textarea name='%s' placeholder=\"请输入内容\" class=\"layui-textarea\">%s</textarea>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, val);
    }
}
