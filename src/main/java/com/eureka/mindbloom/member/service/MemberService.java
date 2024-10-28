package com.eureka.mindbloom.member.service;

import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.dto.GetMemberProfileResponse;
import com.eureka.mindbloom.member.dto.SignUpRequest;
import com.eureka.mindbloom.member.dto.SignUpResponse;
import com.eureka.mindbloom.member.dto.UpdateMemberProfileRequest;

public interface MemberService {
    SignUpResponse signUp(SignUpRequest signUpRequestDto);

    GetMemberProfileResponse getMemberProfile(Member member);

    void updateMemberProfile(Member member, UpdateMemberProfileRequest request);
}
