package com.eureka.mindbloom.member.controller;

import com.eureka.mindbloom.common.dto.ApiResponse;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.dto.GetMemberProfileResponse;
import com.eureka.mindbloom.member.dto.SignUpRequest;
import com.eureka.mindbloom.member.dto.SignUpResponse;
import com.eureka.mindbloom.member.dto.UpdateMemberProfileRequest;
import com.eureka.mindbloom.member.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@Valid @RequestBody SignUpRequest request) {
        SignUpResponse response = memberService.signUp(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("회원가입이 성공적으로 완료되었습니다.", response));
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<?>> getMemberProfile(
            @AuthenticationPrincipal(expression = "member") Member member
    ) {
        GetMemberProfileResponse response = memberService.getMemberProfile(member);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("OK", response));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<Void>> updateMemberProfile(
            @AuthenticationPrincipal(expression = "member") Member member,
            @Valid @RequestBody UpdateMemberProfileRequest request
    ){
        memberService.updateMemberProfile(member, request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(ApiResponse.success("OK"));
    }
}
