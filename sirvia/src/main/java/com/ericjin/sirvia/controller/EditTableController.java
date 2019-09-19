package com.ericjin.sirvia.controller;

import com.ericjin.sirvia.service.EditTableService;
import com.ericjin.sirvia.service.IndexService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class EditTableController {
    @Resource(name = "indexService")
    private IndexService indexService;

    @Resource(name = "editTableService")
    private EditTableService editTableService;

    /**
     * 展示编辑表单
     *
     * @param modelName
     * @param beanName
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/{model}/{bean_name}/edit/{id}")
    public String editTableDisplay(@PathVariable("model") String modelName, @PathVariable("bean_name") String beanName,
                                   @PathVariable("id") Integer id, Model model) {
        // 获取表名
        String tableName = indexService.getShowName(modelName, beanName);
        model.addAttribute("tableName", tableName);
        model.addAttribute("modelName", modelName);
        model.addAttribute("beanName", beanName);
        model.addAttribute("id", id);
        // 获取表达的内容
        model.addAttribute("inputStr", editTableService.getOneData(modelName, beanName, id));
        return "edit_table";
    }


    /**
     * 更新数据
     *
     * @param modelName
     * @param beanName
     * @param id
     * @return
     */
    @ResponseBody
    @PostMapping("/{model}/{bean_name}/edit/{id}")
    public String editTable(@PathVariable("model") String modelName, @PathVariable("bean_name") String beanName,
                            @PathVariable("id") Integer id, @RequestParam Map<String, Object> map) {
        return editTableService.updateTable(modelName, beanName, id, map) ? "1" : "0";
    }


    /**
     * 删除数据
     *
     * @param modelName
     * @param beanName
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/{model}/{bean_name}/del/{id}")
    public String deleteTable(@PathVariable("model") String modelName, @PathVariable("bean_name") String beanName,
                              @PathVariable("id") Integer id) {
        return editTableService.deleteTable(modelName, beanName, id) ? "1" : "0";
    }
}
