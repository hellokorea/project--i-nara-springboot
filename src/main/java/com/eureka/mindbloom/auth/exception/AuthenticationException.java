package com.eureka.mindbloom.auth.exception;

import org.springframework.http.HttpStatus;

public class AuthenticationException extends BaseException {
    private static final String UNAUTHENTICATED_TOKEN = "토큰 '%s'는 유효하지 않습니다.";

    public AuthenticationException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }

    public static AuthenticationException unauthenticatedToken(String token) {
        return new AuthenticationException(String.format(UNAUTHENTICATED_TOKEN, token));
    }
}
