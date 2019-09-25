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

    /**
     * <p>
     *     list添加一行新的数据 <br>
     * </p>
     * @param key 键值
     * @param content 内容
     * @return 1或者0
     */
    @ResponseBody
    @PostMapping("/add_line_list")
    public String addNewLineList(String key, String content) {
        return redisModifyService.addLineList(key, content) > 0 ? "1" : "0";
    }

    /**
     * <p>
     *     移除指定行数的set
     * </p>
     * @param key 键值
     * @param list 删除的值
     * @return 返回1
     */
    @ResponseBody
    @PostMapping("/remove_line_set")
    public String removeLineSet(String key, @RequestParam("list[]") String[] list) {
        redisModifyService.removeLineSet(key, list);
        return "1";
    }

    /**
     * <p>
     *     添加新行到set
     * </p>
     * @param key 键值
     * @param content 内容
     * @return 返回1
     */
    @ResponseBody
    @PostMapping("/add_line_set")
    public String addLineSet(String key, String content) {
        redisModifyService.addLineSet(key, content);
        return "1";
    }

    /**
     * <p>
     *     删除zset中指定行
     * </p>
     * @param key 键值
     * @param list 要删除的value
     * @return 返回1
     */
    @ResponseBody
    @PostMapping("/remove_line_zset")
    public String removeLineZset(String key, @RequestParam("list[]") String[] list) {
        redisModifyService.removeLineZset(key, list);
        return "1";
    }

    /**
     * <p>
     *     新增数据到zset中
     * </p>
     * @param key 键值
     * @param content 内容
     * @param score 分数
     * @return 返回1
     */
    @ResponseBody
    @PostMapping("/add_line_zset")
    public String addLineZset(String key, String content, String score) {
        redisModifyService.addLineZset(key, content, score);
        return "1";
    }

    /**
     * <p>
     *     移除hash中指定的行
     * </p>
     * @param key 键值
     * @param list 选中的行
     * @return 返回1
     */
    @ResponseBody
    @PostMapping("/remove_line_hash")
    public String removeLineHash(String key, @RequestParam("list[]") String[] list) {
        redisModifyService.removeLineHash(key, list);
        return "1";
    }

    @ResponseBody
    @PostMapping("/add_line_hash")
    public String addLineHash(String key, String hashKey, String content) {
        redisModifyService.addLineHash(key, hashKey, content);
        return "1";
    }
}
