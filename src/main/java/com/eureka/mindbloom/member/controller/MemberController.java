package com.eureka.mindbloom.member.controller;

import com.eureka.mindbloom.auth.dto.SignUpRequest;
import com.eureka.mindbloom.auth.dto.SignUpResponse;
import com.eureka.mindbloom.common.dto.ApiResponse;
import com.eureka.mindbloom.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@Valid @RequestBody SignUpRequest requestDto) {
        SignUpResponse responseDto = memberService.signUp(requestDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입이 성공적으로 완료되었습니다.", responseDto));
    }
}
