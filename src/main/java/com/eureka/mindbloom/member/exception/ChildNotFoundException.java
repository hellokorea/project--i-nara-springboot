package com.eureka.mindbloom.member.exception;

import com.eureka.mindbloom.common.exception.NotFoundException;

public class ChildNotFoundException extends NotFoundException {
    private static final String CHILD = "자녀 정보가 존재하지 않습니다. Id: %s";

    public ChildNotFoundException(Long childId) {
        super(String.format(CHILD, childId));
    }
}
