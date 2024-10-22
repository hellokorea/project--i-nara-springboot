package com.eureka.mindbloom.auth.dto;

public record MemberLoginRequest(
        String email,
        String password
) {
}