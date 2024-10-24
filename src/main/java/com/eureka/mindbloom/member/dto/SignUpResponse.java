package com.eureka.mindbloom.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SignUpResponse {
    private final Long memberId;
    private final String email;
    private final String name;

    @Builder
    public SignUpResponse(Long memberId, String email, String name) {
        this.memberId = memberId;
        this.email = email;
        this.name = name;
    }
}
