package com.ericjin.javadmin.controller;

import com.ericjin.javadmin.service.IndexService;
import com.ericjin.javadmin.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class IndexController {
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "indexService")
    private IndexService indexService;

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
                              @PathVariable("table_name") String tableName) {
        List<Map<String, Object>> singleTable = indexService.getSingleTable(modelName, tableName);
        model.addAttribute("dataList", singleTable);
        // 获取表名
        model.addAttribute("tableName", indexService.getShowName(modelName, tableName));
        model.addAttribute("modelName", modelName);
        model.addAttribute("beanName", tableName);
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
}
