package com.eureka.mindbloom.member.exception;


public class DuplicationEmailException extends ConflictException {
    private static final String DUPLICATION_EMAIL_MESSAGE = "이메일 '%s'는 이미 사용 중입니다.";

    public DuplicationEmailException(String message) {
        super(message);
    }

    public static DuplicationEmailException emailAlreadyExists(String email) {
        return new DuplicationEmailException(String.format(DUPLICATION_EMAIL_MESSAGE, email));
    }
}
