package com.ericjin.javadmin.controller;

import com.ericjin.javadmin.service.TableListService;
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
     * @param methodName
     * @param selectArr
     * @return
     */
    @ResponseBody
    @PostMapping("/action/{modelName}/{beanName}")
    public String doAction(@RequestParam("method_name") String methodName, @RequestParam("select_arr[]") List<Integer> selectArr,
                           @PathVariable String modelName, @PathVariable String beanName) {
        return tableListService.doAction(selectArr, methodName, modelName, beanName) ? "1" : "0";
    }
}
