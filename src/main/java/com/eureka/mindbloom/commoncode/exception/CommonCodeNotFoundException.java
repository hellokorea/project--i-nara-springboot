package com.eureka.mindbloom.commoncode.exception;

import com.eureka.mindbloom.common.exception.NotFoundException;

public class CommonCodeNotFoundException extends NotFoundException {
    private static final String MESSAGE_FORMAT = "존재하지 않는 공통코드 \"%s\" 입니다";

    public CommonCodeNotFoundException(String code) {
        super(String.format(MESSAGE_FORMAT, code));
    }
}
