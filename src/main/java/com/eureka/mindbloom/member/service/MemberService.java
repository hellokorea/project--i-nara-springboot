package com.eureka.mindbloom.member.service;

import com.eureka.mindbloom.member.dto.SignUpResponse;
import com.eureka.mindbloom.member.dto.SignUpRequest;

public interface MemberService {
    SignUpResponse signUp(SignUpRequest signUpRequestDto);
}
