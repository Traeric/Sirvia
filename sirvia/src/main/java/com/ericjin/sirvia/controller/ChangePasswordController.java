package com.ericjin.sirvia.controller;

import com.ericjin.sirvia.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/admin/change_password")
public class ChangePasswordController {
    @Resource(name = "userService")
    private UserService userService;

    /**
     * 修改密码展示页面
     *
     * @return
     */
    @GetMapping
    public String changePasswordDisplay() {
        return "change_password";
    }

    /**
     * 验证旧密码
     *
     * @param oldPassword
     * @param userId
     * @return
     */
    @ResponseBody
    @PostMapping("/old_password")
    public String confirmOldPassword(@RequestParam("old_password") String oldPassword, @RequestParam("user_id") String userId) {
        return userService.confirmOldPassword(userId, oldPassword) ? "1" : "0";
    }

    /**
     * 修改密码
     *
     * @return
     */
    @ResponseBody
    @PostMapping("/change")
    public String changePassword(@RequestParam("user_id") String userId, @RequestParam("new_password") String newPassword) {
        return userService.changePassword(userId, newPassword) ? "1" : "0";
    }
}
