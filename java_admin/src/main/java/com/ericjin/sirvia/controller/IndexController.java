package com.ericjin.sirvia.controller;

import com.ericjin.sirvia.service.IndexService;
import com.ericjin.sirvia.service.UserService;
import com.ericjin.sirvia.utils.ToCamelCase;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.util.*;

@Controller
@RequestMapping("/admin")
public class IndexController {
    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "indexService")
    private IndexService indexService;

    @Resource(name = "actionMap")
    private Map<String, String> actionMap;

    /**
     * 后台首页
     *
     * @return
     */
    @GetMapping
    public String index(Model model) {
        // 获取表名称
        Map<String, List<Map<String, String>>> tableList = userService.getTableList();
        model.addAttribute("systemTable", tableList.get("systemTable"));
        model.addAttribute("userTable", tableList.get("userTable"));
        return "admin";
    }

    /**
     * 获取单个表的详细信息
     *
     * @param model
     * @param modelName
     * @param tableName
     * @return
     */
    @GetMapping("/{model}/{table_name}")
    public String singleTable(Model model, @PathVariable("model") String modelName,
                              @PathVariable("table_name") String tableName, @RequestParam(value = "page", defaultValue = "1") Integer page) {
        PageHelper.startPage(page, 10);   // 每页显示10条数据
        List<Map<String, Object>> singleTable = indexService.getSingleTable(modelName, tableName);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(singleTable, 5);    // 连续显示5页
        model.addAttribute("pageInfo", pageInfo);    // 分页信息
        model.addAttribute("dataList", singleTable);
        // 获取属性名
        Class bean = indexService.getBean(modelName, tableName);
        Field[] fields = bean.getDeclaredFields();
        List<Field> fieldList = Arrays.asList(fields);
        List<String> list = new LinkedList<>();
        // 处理属性名
        fieldList.forEach((field) -> {
            String toLowField = ToCamelCase.humpToLine(field.getName());
            // 排除掉不在map中的字段
            Set<String> strings = singleTable.get(0).keySet();
            if (strings.contains(toLowField)) {
                list.add(toLowField);
            }
        });
        model.addAttribute("fields", list);
        // 获取表名
        model.addAttribute("tableName", indexService.getShowName(modelName, tableName));
        model.addAttribute("modelName", modelName);
        model.addAttribute("beanName", tableName);
        // 获取action映射
        model.addAttribute("actionMap", actionMap);
        return "table_list";
    }

    /**
     * 展示添加列表
     *
     * @param modelName
     * @param beanName
     * @param model
     * @return
     */
    @GetMapping("/{model}/{table_name}/add")
    public String addTableDisplay(@PathVariable("model") String modelName,
                                  @PathVariable("table_name") String beanName, Model model) {
        model.addAttribute("tableName", indexService.getShowName(modelName, beanName));
        model.addAttribute("modelName", modelName);
        model.addAttribute("beanName", beanName);
        // 获取表单展示内容
        String inputList = indexService.getInputList(modelName, beanName);
        model.addAttribute("inputList", inputList);
        return "add_table";
    }

    /**
     * 添加数据到表中
     *
     * @param modelName
     * @param beanName
     * @return
     */
    @ResponseBody
    @PostMapping("/{model}/{table_name}/add")
    public String addTable(@PathVariable("model") String modelName, @PathVariable("table_name") String beanName,
                           @RequestParam Map<String, Object> map) {
        if (indexService.addTable(map, modelName, beanName)) {
            return "1";
        }
        return "0";
    }

    /**
     * 获取数据库中所有的表
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/get_all_tables")
    public Map<String, Object> getAllTables() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("flag", true);
        map.put("list", indexService.getAllTables());
        return map;
    }

    /**
     * 执行sql语句
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/execute_sql")
    public String executeSql(String sql) {
        try {
            indexService.executeSql(sql);
        } catch (Exception e) {
            return "0";
        }
        return "1";
    }
}
