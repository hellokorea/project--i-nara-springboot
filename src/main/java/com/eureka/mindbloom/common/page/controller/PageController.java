package com.eureka.mindbloom.common.page.controller;

import com.eureka.mindbloom.member.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class PageController {

    private final ChildService childService;

    @GetMapping("/signup")  // 회원가입 페이지
    public String signupPage() {
        return "signup";
    }

    @GetMapping("/login")   // 로그인 페이지
    public String loginPage() {
        return "login";
    }

    @GetMapping("/learn")
    public String learnPage() {
        return "learn";
    }

    @GetMapping("/main")
    public String mainPage() {
        return "main";
    }

    @GetMapping("/profile") // 프로필 선택 페이지
    public String profilePage() {
        return "profile"; // 프로필 선택 페이지 템플릿 이름
    }

    @GetMapping("/profile/create") // 프로필 생성 페이지
    public String createProfilePage() {
        return "child-profile";
    }

    @GetMapping("/detail/{isbn}")
    public String bookDetailPage(@PathVariable String isbn) {
        return "bookdetail";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}