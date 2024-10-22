package com.eureka.mindbloom.member.controller;

import com.eureka.mindbloom.common.dto.ApiResponse;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.dto.ChildRegisterRequest;
import com.eureka.mindbloom.member.dto.ChildRegisterResponse;
import com.eureka.mindbloom.member.service.ChildService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChildController {

    private final ChildService childService;

    @PostMapping("/members/children")
    public ResponseEntity<ApiResponse<?>> registerChild(
            @AuthenticationPrincipal(expression = "member") Member member,
            @RequestBody ChildRegisterRequest request) {

        ChildRegisterResponse response = childService.registerChild(member, request);

        return ResponseEntity.ok().body(ApiResponse.success("자녀 프로필이 생성되었습니다.", response));
    }
}