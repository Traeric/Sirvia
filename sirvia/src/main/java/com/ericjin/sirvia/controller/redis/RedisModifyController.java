package com.ericjin.sirvia.controller.redis;

import com.ericjin.sirvia.service.RedisModifyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
@RequestMapping("/admin/redis")
public class RedisModifyController {
    @Resource(name = "redisModifyServiceImpl")
    private RedisModifyService redisModifyService;

    /**
     * <p>
     *     修改string类型的值 <br>
     * </p>
     * @param key 要修改的键的名称
     * @param content 更新的内容
     * @return 返回0或者1
     */
    @ResponseBody
    @PostMapping("/save_string")
    public String saveString(String key, String content) {
        return "OK".equals(redisModifyService.saveString(key, content)) ? "1" : "0";
    }

    /**
     * <p>
     *     修改键值 <br>
     * </p>
     * @param key 旧的键
     * @param newKey 新的键
     * @return 返回json对象
     */
    @ResponseBody
    @PostMapping("/reload_key_string")
    public Map<String, Object> reloadString(String key, String newKey) {
        Map<String, Object> res = new LinkedHashMap<>();
        String msg = redisModifyService.reloadKeyString(key, newKey);
        res.put("msg", msg);
        res.put("flag", true);
        return res;
    }

    /**
     * <p>
     *     删除某个键值为key的数据 <br>
     * </p>
     * @param key 要删除的键值
     * @return 返回0或者1
     */
    @ResponseBody
    @PostMapping("/remove")
    public String remove(String key) {
        return redisModifyService.remove(key) == 1 ? "1" : "0";
    }

    /**
     * <p>
     *     删除选中的行 针对列表
     * </p>
     * @param key 键值
     * @param list 选中的行的index
     * @return 返回0或者1
     */
    @ResponseBody
    @PostMapping("/remove_line")
    public String removeCheckedLine(String key, @RequestParam("list[]") Long[] list) {
        redisModifyService.removeLine(key, list);
        return "1";
    }

    @ResponseBody
    @PostMapping("/add_line_list")
    public String addNewLineList(String key, String content) {
        return redisModifyService.addLineList(key, content) > 0 ? "1" : "0";
    }
}
