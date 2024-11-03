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

    @GetMapping("/nature")
    public String naturePage() {
        return "nature";
    }

    @GetMapping("/daily-life")
    public String dailylifePage() {
        return "daily-life";
    }

    @GetMapping("/stories")
    public String storiesePage() {
        return "stories";
    }

    @GetMapping("/animals")
    public String animalsPage() {
        return "animals";
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

    @GetMapping("/search")
    public String searchPage() { return "search"; }

    @GetMapping("/detail/{isbn}")
    public String bookDetailPage(@PathVariable String isbn) {
        return "bookdetail";
    }

    @GetMapping("/admin")
    public String adminPage() {
        return "adminmain";
    }

    @GetMapping("/childdetail")
    public String childDetailPage() { return "detail-profile"; }
}