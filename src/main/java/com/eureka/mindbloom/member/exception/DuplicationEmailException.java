package com.eureka.mindbloom.member.exception;

import com.eureka.mindbloom.common.exception.BaseException;
import org.springframework.http.HttpStatus;

public class DuplicationEmailException extends BaseException {
    public DuplicationEmailException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
