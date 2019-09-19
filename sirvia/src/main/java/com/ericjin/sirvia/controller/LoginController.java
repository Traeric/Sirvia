package com.ericjin.sirvia.controller;


import com.ericjin.sirvia.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

@Controller
@RequestMapping("/admin")
public class LoginController {
    @Resource(name = "userService")
    private UserService userService;


    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * 验证用户登陆
     * @param model
     * @param email
     * @param password
     * @return
     */
    @PostMapping("/login")
    public String checkLogin(Model model, String email, String password) {
        if (userService.checkLogin(email, password)) {
            return "redirect:/admin";
        }
        model.addAttribute("error", "用户名或者密码错误！");
        return "login";
    }
}
