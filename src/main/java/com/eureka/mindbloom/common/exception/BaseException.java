package com.eureka.mindbloom.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class BaseException extends RuntimeException {
    private final HttpStatusCode status;

    public BaseException(String message, HttpStatusCode status) {
        super(message);
        this.status = status;
    }

    public BaseException(String message, Throwable cause, HttpStatusCode status) {
        super(message, cause);
        this.status = status;
    }
}
