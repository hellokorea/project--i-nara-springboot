package com.eureka.mindbloom.member.exception;

import com.eureka.mindbloom.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class ConflictException extends BaseException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
