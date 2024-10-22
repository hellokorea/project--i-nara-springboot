package com.eureka.mindbloom.member.service;

import com.eureka.mindbloom.auth.dto.SignUpResponse;
import com.eureka.mindbloom.auth.dto.SignUpRequest;

public interface MemberService {
    SignUpResponse signUp(SignUpRequest signUpRequestDto);
}
