package com.jyou.thinker.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * TODO: 系统登录控制层
 * @author wgbing
 * @date 2018/7/10 15:35
 */
@Slf4j
@Controller
public class LoginController {

    @RequestMapping("/login")
    public String login() {
        return "/login";
    }

    @GetMapping("/index")
    public String index() {
        return "/index";
    }

    @GetMapping("/main")
    public String welcome() {
        return "/main";
    }

}
