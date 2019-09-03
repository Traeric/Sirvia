package com.ericjin.javadmin.utils;

import com.ericjin.javadmin.Settings;
import com.ericjin.javadmin.annotation.Choose;
import com.ericjin.javadmin.annotation.DateUse;
import com.ericjin.javadmin.annotation.Password;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Map;

public class FieldToInputStr {
    /**
     * 通过字段类型获取对应的表单
     *
     * @param field
     * @return
     */
    public static String getInputStr(Field field) {
        Class type = field.getType();
        String fieldName = ToCamelCase.humpToLine(field.getName());
        if (type == Integer.class || type == int.class || type == String.class || type == Long.class || type == long.class) {
            // 查看该字段上面是否标注了注解
            if (field.isAnnotationPresent(Choose.class)) {
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
            } else if (field.isAnnotationPresent(DateUse.class)) {
                return String.format("<div class=\"layui-inline\">\n" +
                        "            <label class=\"layui-form-label\">%s:</label>\n" +
                        "            <div class=\"layui-input-inline\">\n" +
                        "                <input type=\"text\" name=\"%s\" id=\"date\" lay-verify=\"date\" placeholder=\"yyyy-MM-dd\" autocomplete=\"off\"\n" +
                        "                       class=\"layui-input\">\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "        <hr class=\"layui-bg-gray\">", fieldName, fieldName);
            } else if (field.isAnnotationPresent(Password.class)) {
                return String.format("<div class='layui-form-item'>\n" +
                        "            <label class='layui-form-label'>%s:</label>\n" +
                        "            <div class='layui-input-inline'>\n" +
                        "                <input type='password' name='%s' lay-verify='title' autocomplete='off' placeholder='请输入%s'\n" +
                        "            class='layui-input'>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, fieldName);
            } else {
                return String.format("<div class='layui-form-item'>\n" +
                        "            <label class='layui-form-label'>%s:</label>\n" +
                        "            <div class='layui-input-inline'>\n" +
                        "                <input type='text' name='%s' lay-verify='title' autocomplete='off' placeholder='请输入%s'\n" +
                        "            class='layui-input'>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, fieldName);
            }

        } else if (type == Boolean.class || type == boolean.class) {
            return String.format("<div class=\"layui-form-item\">\n" +
                    "            <label class=\"layui-form-label\">%s:</label>\n" +
                    "            <div class=\"layui-input-block\">\n" +
                    "                <input type=\"checkbox\" name=\"%s\" title=\"%s\">\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, fieldName);
        } else if (type == Date.class) {
            return String.format("<div class=\"layui-inline\">\n" +
                    "            <label class=\"layui-form-label\">%s:</label>\n" +
                    "            <div class=\"layui-input-inline\">\n" +
                    "                <input type=\"text\" name=\"%s\" id=\"date\" lay-verify=\"date\" placeholder=\"yyyy-MM-dd\" autocomplete=\"off\"\n" +
                    "                       class=\"layui-input\">\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "        <hr class=\"layui-bg-gray\">", fieldName, fieldName);
        } else if (type == StringBuffer.class || type == StringBuilder.class) {
            return String.format("<div class=\"layui-form-item layui-form-text\">\n" +
                    "            <label class=\"layui-form-label\">%s:</label>\n" +
                    "            <div class=\"layui-input-block\">\n" +
                    "                <textarea name='%s' placeholder=\"请输入内容\" class=\"layui-textarea\"></textarea>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "        <hr class=\"layui-bg-gray\">", fieldName, fieldName);
        }
        return String.format("<div class='layui-form-item'>\n" +
                "            <label class='layui-form-label'>%s:</label>\n" +
                "            <div class='layui-input-inline'>\n" +
                "                <input type='text' name='%s' lay-verify='title' autocomplete='off' placeholder='请输入%s'\n" +
                "            class='layui-input'>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, fieldName);
    }

    /**
     * 通过字段类型确定表单，并且填值
     * @param field
     * @return
     */
    public static String getInputStrWithValue(Field field, Map<String, Object> map) {
        Class type = field.getType();
        String fieldName = ToCamelCase.humpToLine(field.getName());
        // 获取值
        String val = (String) map.get(fieldName);
        if (type == Integer.class || type == int.class || type == String.class || type == Long.class || type == long.class) {
            // 查看该字段上面是否标注了注解
            if (field.isAnnotationPresent(Choose.class)) {
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
            } else if (field.isAnnotationPresent(DateUse.class)) {
                return String.format("<div class=\"layui-inline\">\n" +
                        "            <label class=\"layui-form-label\">%s:</label>\n" +
                        "            <div class=\"layui-input-inline\">\n" +
                        "                <input type=\"text\" name=\"%s\" id=\"date\" lay-verify=\"date\" placeholder=\"yyyy-MM-dd\" autocomplete=\"off\"\n" +
                        "                       class=\"layui-input\" value='%s'>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, val);
            } else if (field.isAnnotationPresent(Password.class)) {
                return String.format("<div class='layui-form-item'>\n" +
                        "            <label class='layui-form-label'>%s:</label>\n" +
                        "            <div class='layui-input-inline'>\n" +
                        "                <input type='password' name='%s' lay-verify='title' autocomplete='off' placeholder='请输入%s'\n" +
                        "            class='layui-input' value='%s'>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, fieldName, val);
            } else {
                return String.format("<div class='layui-form-item'>\n" +
                        "            <label class='layui-form-label'>%s:</label>\n" +
                        "            <div class='layui-input-inline'>\n" +
                        "                <input type='text' name='%s' lay-verify='title' autocomplete='off' placeholder='请输入%s'\n" +
                        "            class='layui-input' value='%s'>\n" +
                        "            </div>\n" +
                        "        </div>\n" +
                        "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, fieldName, val);
            }

        } else if (type == Boolean.class || type == boolean.class) {
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
        } else if (type == Date.class) {
            return String.format("<div class=\"layui-inline\">\n" +
                    "            <label class=\"layui-form-label\">%s:</label>\n" +
                    "            <div class=\"layui-input-inline\">\n" +
                    "                <input type=\"text\" name=\"%s\" id=\"date\" lay-verify=\"date\" placeholder=\"yyyy-MM-dd\" autocomplete=\"off\"\n" +
                    "                       class=\"layui-input\" value='%s'>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, val);
        } else if (type == StringBuffer.class || type == StringBuilder.class) {
            return String.format("<div class=\"layui-form-item layui-form-text\">\n" +
                    "            <label class=\"layui-form-label\">%s:</label>\n" +
                    "            <div class=\"layui-input-block\">\n" +
                    "                <textarea name='%s' placeholder=\"请输入内容\" class=\"layui-textarea\">%s</textarea>\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, val);
        }
        return String.format("<div class='layui-form-item'>\n" +
                "            <label class='layui-form-label'>%s:</label>\n" +
                "            <div class='layui-input-inline'>\n" +
                "                <input type='text' name='%s' lay-verify='title' autocomplete='off' placeholder='请输入%s'\n" +
                "            class='layui-input' value='%s'>\n" +
                "            </div>\n" +
                "        </div>\n" +
                "        <hr class=\"layui-bg-gray\">", fieldName, fieldName, fieldName, val);
    }
}
