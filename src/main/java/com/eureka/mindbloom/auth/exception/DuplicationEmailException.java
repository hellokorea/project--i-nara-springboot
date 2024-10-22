package com.eureka.mindbloom.auth.exception;

import org.springframework.http.HttpStatus;

public class DuplicationEmailException extends BaseException{
    public DuplicationEmailException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
