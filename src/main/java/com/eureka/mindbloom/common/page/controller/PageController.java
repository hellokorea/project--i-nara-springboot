package com.eureka.mindbloom.common.page.controller;

import org.springframework.security.access.prepost.PreAuthorize;
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

    @GetMapping("/main")
    @PreAuthorize("isAuthenticated()")  // 인증된 사용자만 접근 가능
    public String mainPage() {
        return "main";
    }

    @GetMapping("/detail/{isbn}")
    public String bookDetailPage(@PathVariable String isbn) {
        return "bookdetail";
    }
}