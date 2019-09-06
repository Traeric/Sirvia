package com.ericjin.javadmin.utils;

import com.ericjin.javadmin.Settings;
import com.ericjin.javadmin.annotation.*;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class FieldToInputStr {
    /**
     * 通过字段类型获取对应的表单
     *
     * @param field
     * @param list
     * @return
     */
    public static String getInputStr(Field field, List<Map<String, Object>> list, String modelName, String beanName) {
        Class type = field.getType();
        String fieldName = ToCamelCase.humpToLine(field.getName());
        if (type == Integer.class || type == int.class || type == String.class || type == Long.class || type == long.class
                || type == List.class) {
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
            } else if (field.isAnnotationPresent(ForeignKey.class)) {
                // 获取键值对
                ForeignKey foreignKey = field.getAnnotation(ForeignKey.class);
                // select类型
                StringBuilder result = new StringBuilder(String.format("<div class=\"layui-inline\">\n" +
                        "      <label class=\"layui-form-label\">%s:</label>\n" +
                        "      <div class=\"layui-input-inline\">\n" +
                        "        <select name=\"%s\" lay-verify=\"required\" lay-search=\"\">", fieldName, fieldName));
                list.forEach(map -> result.append(String.format("<option value=\"%s\">%s</option>",
                        map.get(foreignKey.relation_key()), map.get(foreignKey.show_field()))));
                return result.append(String.format("</select></div>" +
                        "<button type=\"button\" class=\"layui-btn layui-btn-xs layui-btn-warm\" " +
                        "style='margin-left: 10px;' title='添加%s' onclick='openWindow(\"/admin/%s/%s/add\")'>" +
                        "<i class=\"layui-icon layui-icon-add-1\"></i>" +
                        "</button></div><hr class=\"layui-bg-gray\">", fieldName, modelName, beanName)).toString();
            } else if (field.isAnnotationPresent(ManyToManyField.class)) {
                // 获取注解
                ManyToManyField manyToManyField = field.getAnnotation(ManyToManyField.class);
                StringBuilder result = new StringBuilder(String.format("<div class=\"layui-inline\">\n" +
                        "      <label class=\"layui-form-label\">%s:</label>\n" +
                        "      <div class=\"layui-input-inline\">\n" +
                        "      <input type='hidden' name='%s_%s' value=''>\n" +
                        "      <div id=\"transfer_%s\" class=\"demo-transfer\"></div></div>" +
                        "      <button type='button' class='layui-btn layui-btn-xs layui-btn-warm' " +
                        "       style='margin-left: 10px;' title='添加%s' onclick='openWindow(\"/admin/%s/%s/add\")'>" +
                        "          <i class='layui-icon layui-icon-add-1'></i>" +
                        "      </button></div>\n" +
                        "      <script>layui.use(['transfer', 'layer', 'util'], function(){\n" +
                        "        var $=layui.$\n" +
                        "        ,transfer=layui.transfer\n" +
                        "        ,layer=layui.layer\n" +
                        "        ,util=layui.util;\n" +
                        "        let data1=[", fieldName, manyToManyField.third_table(), fieldName, fieldName, fieldName, modelName, beanName));
                // 填充数据
                list.forEach(map1 -> result.append(String.format("{\"value\":\"%s\",\"title\":\"%s\"},\n",
                        map1.get(manyToManyField.relation_field()), map1.get(manyToManyField.show_field()))));
                return result.append(String.format("]\n" +
                                "        transfer.render({\n" +
                                "        elem:'#transfer_%s'\n" +
                                "        ,data:data1\n" +
                                "        ,title:['可供选择','已经选择']\n" +
                                "        ,showSearch:true,\n" +
                                "        id: '%s',\n" +
                                "        onchange(data, index) {\n" +
                                "            let selectValue = transfer.getData('%s');\n" +
                                "            let values = '';\n" +
                                "            $.each(selectValue, (index, item) => {\n" +
                                "                values += item['value'] + ' ';\n" +
                                "            });\n" +
                                "            $('input[name=%s_%s]').val(values)\n" +
                                "        }\n" +
                                "        })\n" +
                                "});</script><hr class=\"layui-bg-gray\">", fieldName, fieldName, fieldName, manyToManyField.third_table(),
                        fieldName)).toString();
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
     * 重载getInputStr
     *
     * @param field
     * @return
     */
    public static String getInputStr(Field field) {
        return FieldToInputStr.getInputStr(field, null, "", "");
    }

    /**
     * 通过字段类型确定表单，并且填值
     *
     * @param field
     * @param map
     * @param list
     * @return
     */
    public static String getInputStrWithValue(Field field, Map<String, Object> map, List<Map<String, Object>> list,
                                              List<Map<String, String>> selectData, String modelName, String beanName) {
        Class type = field.getType();
        String fieldName = ToCamelCase.humpToLine(field.getName());
        // 由于是修改，需要获取已经有的值
        String val = String.valueOf(map.get(fieldName));
        if (type == Integer.class || type == int.class || type == String.class || type == Long.class || type == long.class
                || type == List.class) {
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
            } else if (field.isAnnotationPresent(ForeignKey.class)) {
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
                        "title='添加%s' onclick='openWindow(\"/admin/%s/%s/add\")'>" +
                        "<i class='layui-icon layui-icon-add-1'></i>" +
                        "</button></div><hr class=\"layui-bg-gray\">", fieldName, modelName, beanName)).toString();
            } else if (field.isAnnotationPresent(ManyToManyField.class)) {
                // 获取注解
                ManyToManyField manyToManyField = field.getAnnotation(ManyToManyField.class);
                // 将已经选中的数据填入input框中
                StringBuilder values = new StringBuilder();
                StringBuilder transferValue = new StringBuilder("[");
                for (Map<String, String> item : selectData) {
                    values.append(String.valueOf(item.get(manyToManyField.third_relation_field()))).append(" ");
                    transferValue.append(String.format("'%s', ", item.get(manyToManyField.third_relation_field())));
                }
                transferValue.append("]");
                StringBuilder result = new StringBuilder(String.format("<div class=\"layui-inline\">\n" +
                        "      <label class=\"layui-form-label\">%s:</label>\n" +
                        "      <div class=\"layui-input-inline\">\n" +
                        "      <input type='hidden' name='%s_%s' value='%s'>\n" +
                        "      <div id=\"transfer_%s\" class=\"demo-transfer\"></div></div>" +
                        "      <button type='button' class='layui-btn layui-btn-xs layui-btn-warm' style='margin-left: 10px;' " +
                        "       title='添加%s' onclick='openWindow(\"/admin/%s/%s/add\")'>" +
                        "          <i class='layui-icon layui-icon-add-1'></i>" +
                        "      </button>" +
                        "      </div>\n" +
                        "      <script>layui.use(['transfer', 'layer', 'util'], function(){\n" +
                        "        var $=layui.$\n" +
                        "        ,transfer=layui.transfer\n" +
                        "        ,layer=layui.layer\n" +
                        "        ,util=layui.util;\n" +
                        "        let data1=[", fieldName, manyToManyField.third_table(), fieldName, values.toString(), fieldName, fieldName, modelName, beanName));
                // 填充数据
                list.forEach(map1 -> result.append(String.format("{\"value\":\"%s\",\"title\":\"%s\"},\n",
                        map1.get(manyToManyField.relation_field()), map1.get(manyToManyField.show_field()))));
                return result.append(String.format("]\n" +
                                "        transfer.render({\n" +
                                "        elem:'#transfer_%s'\n" +
                                "        ,data:data1\n" +
                                "        ,title:['可供选择','已经选择']\n" +
                                "        ,showSearch:true,\n" +
                                "        id: '%s',\n" +
                                "        value: %s,\n" +
                                "        onchange(data, index) {\n" +
                                "            let selectValue = transfer.getData('%s');\n" +
                                "            let values = '';\n" +
                                "            $.each(selectValue, (index, item) => {\n" +
                                "                values += item['value'] + ' ';\n" +
                                "            });\n" +
                                "            $('input[name=%s_%s]').val(values)\n" +
                                "        }\n" +
                                "        })\n" +
                                "});</script><hr class=\"layui-bg-gray\">", fieldName, fieldName, transferValue.toString(), fieldName,
                        manyToManyField.third_table(), fieldName)).toString();
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

    /**
     * getInputStrWithValue
     *
     * @param field
     * @param map
     * @return
     */
    public static String getInputStrWithValue(Field field, Map<String, Object> map) {
        return FieldToInputStr.getInputStrWithValue(field, map, null);
    }

    /**
     * @param field
     * @param map
     * @param list
     * @return
     */
    public static String getInputStrWithValue(Field field, Map<String, Object> map, List<Map<String, Object>> list) {
        return FieldToInputStr.getInputStrWithValue(field, map, list, null, "", "");
    }
}
