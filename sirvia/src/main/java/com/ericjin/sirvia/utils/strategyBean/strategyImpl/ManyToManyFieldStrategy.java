package com.ericjin.sirvia.utils.strategyBean.strategyImpl;

import com.ericjin.sirvia.annotation.ManyToManyField;
import com.ericjin.sirvia.utils.strategyBean.BeanStrategy;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

public class ManyToManyFieldStrategy implements BeanStrategy {
    @Override
    public String getEmptyInput(Map<String, Object> map) {
        // 解构数据
        Field field = (Field) map.get("field");
        String fieldName = (String) map.get("fieldName");
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
        String modelName = (String) map.get("modelName");
        String beanName = (String) map.get("beanName");
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
    }

    @Override
    public String getFullInput(Map<String, Object> map) {
        // 解构数据
        Field field = (Field) map.get("field");
        String fieldName = (String) map.get("fieldName");
        List<Map<String, Object>> list = (List<Map<String, Object>>) map.get("list");
        String modelName = (String) map.get("modelName");
        String beanName = (String) map.get("beanName");
        List<Map<String, String>> selectData = (List<Map<String, String>>) map.get("selectData");
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
                "       title='添加%s' onclick='openWindow(\"/redis/%s/%s/add\")'>" +
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
    }
}
