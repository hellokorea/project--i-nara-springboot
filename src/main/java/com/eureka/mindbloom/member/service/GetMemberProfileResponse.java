package com.eureka.mindbloom.member.service;

import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.dto.ChildProfileResponse;

import java.util.List;

public record GetMemberProfileResponse(
        String email,
        String name,
        List<ChildProfileResponse> childrenProfiles
) {
    public static GetMemberProfileResponse from(Member member) {
        List<ChildProfileResponse> profiles = member.getChildren().stream().map(ChildProfileResponse::from).toList();

        return new GetMemberProfileResponse(member.getEmail(), member.getName(), profiles);
    }
}