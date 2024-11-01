package com.eureka.mindbloom.common.page.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // login.html 파일을 렌더링
    }

    @GetMapping("/signup")
    public String signupPage() {
        return "signup"; // signup.html 파일을 렌더링 (추후 구현)
    }

    @GetMapping("/learn")
    public String learnPage() {
        return "learn";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }
    @GetMapping("/detail/{isbn}")
    public String bookDetailPage(@PathVariable String isbn) {
        return "bookdetail";
    }
}