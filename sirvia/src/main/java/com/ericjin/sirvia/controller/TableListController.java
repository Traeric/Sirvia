package com.ericjin.sirvia.controller;

import com.ericjin.sirvia.service.TableListService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class TableListController {
    @Resource(name = "tableListServiceImpl")
    private TableListService tableListService;

    /**
     * 执行action
     *
     * @param methodName 执行的方法名
     * @param selectArr 选中的数据
     * @return 0或者1
     */
    @ResponseBody
    @PostMapping("/action/{modelName}/{beanName}")
    public String doAction(@RequestParam("method_name") String methodName, @RequestParam("select_arr[]") List<String> selectArr,
                           @PathVariable String modelName, @PathVariable String beanName) {
        return tableListService.doAction(selectArr, methodName, modelName, beanName) ? "1" : "0";
    }


    /**
     * 获取对应javabean中所有的字段列表
     * @param modelName 模型名 用户表还是系统表
     * @param beanName javabean名称
     * @return 返回字段列表
     */
    @ResponseBody
    @PostMapping("/get_field/{modelName}/{beanName}")
    public List<String> getAllField(@PathVariable String modelName, @PathVariable String beanName) {
        return tableListService.getAllField(modelName, beanName);
    }

    /**
     * 生成数据
     * @param dataNumbers 要生成的数据条数
     * @param fieldsList 要生成的字段
     * @return String
     */
    @ResponseBody
    @PostMapping("/generate_data")
    public String generateData(@RequestParam("dataNumbers") Integer dataNumbers,
                               @RequestParam("fieldsList[]") List<String> fieldsList) {
        System.out.println(dataNumbers);
        System.out.println(fieldsList);
        return "hhhjk";
    }
}
