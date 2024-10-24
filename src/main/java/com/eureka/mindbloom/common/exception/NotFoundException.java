package com.eureka.mindbloom.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    private static final String CHILD = "자녀 정보가 존재하지 않습니다. %s";

    private NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public static NotFoundException childNotFound(Long childId) {
        return new NotFoundException(String.format(CHILD, childId));
    }
}
