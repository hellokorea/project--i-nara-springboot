package com.eureka.mindbloom.common.exception;

import org.springframework.http.HttpStatus;

public class DuplicationEmailException extends BaseException{
    public DuplicationEmailException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
