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
     * @param modelName 模型名
     * @param beanName javabean名称
     * @param id id值
     * @param model 数据模型
     * @return HTML文件名称
     */
    @GetMapping("/{model}/{bean_name}/edit/{id}")
    public String editTableDisplay(@PathVariable("model") String modelName, @PathVariable("bean_name") String beanName,
                                   @PathVariable("id") String id, Model model) {
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
     * @param modelName 模型名
     * @param beanName  javabean名称
     * @param id id
     * @return 0或者1
     */
    @ResponseBody
    @PostMapping("/{model}/{bean_name}/edit/{id}")
    public String editTable(@PathVariable("model") String modelName, @PathVariable("bean_name") String beanName,
                            @PathVariable("id") String id, @RequestParam Map<String, Object> map) {
        return editTableService.updateTable(modelName, beanName, id, map) ? "1" : "0";
    }

    /**
     * 获取删除页面
     * @param modelName 模型名
     * @param beanName javabean名称
     * @param id 数据id
     * @return 返回html页面的值
     */
    @GetMapping("/{model}/{bean_name}/del/{id}")
    public String deletePage(@PathVariable("model") String modelName, @PathVariable("bean_name") String beanName,
                             @PathVariable("id") String id, Model model) {
        // 获取表名
        String tableName = indexService.getShowName(modelName, beanName);
        model.addAttribute("modelName", modelName);
        model.addAttribute("beanName", beanName);
        model.addAttribute("tableName", tableName);
        model.addAttribute("id", id);
        // 获取删除的信息
        model.addAttribute("deleteInfo", editTableService.getDeleteRelationInfo(modelName, beanName, id));
        return "delete_table";
    }

    /**
     * 删除数据
     *
     * @param modelName 模型名
     * @param beanName javabean名
     * @param id id
     * @return 0或者1
     */
    @ResponseBody
    @DeleteMapping("/{model}/{bean_name}/del/{id}")
    public String deleteTable(@PathVariable("model") String modelName, @PathVariable("bean_name") String beanName,
                              @PathVariable("id") String id) {
        return editTableService.deleteTable(modelName, beanName, id) ? "1" : "0";
    }

    @PostMapping(value = "/{model}/{bean}/{field}/{id}/edit_single_input", produces = "text/plain;charset=utf-8")
    @ResponseBody
    public String editSingleInput(@PathVariable("model") String modelName, @PathVariable("bean") String beanName,
                                  @PathVariable("field") String fieldName, @PathVariable("id") String id) {
        return editTableService.editSingleInput(modelName, beanName, fieldName, id);
    }
}
