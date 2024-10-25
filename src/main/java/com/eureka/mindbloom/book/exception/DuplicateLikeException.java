package com.eureka.mindbloom.book.exception;

import com.eureka.mindbloom.common.exception.ConflictException;

public class DuplicateLikeException extends ConflictException {
    private static final String DUPLICATE_LIKE_MESSAGE = "도서 '%s'에 대한 좋아요가 이미 존재합니다.";

    public DuplicateLikeException(String message) {
        super(message);
    }

    public static DuplicateLikeException likeAlreadyExists(String isbn) {
        return new DuplicateLikeException(String.format(DUPLICATE_LIKE_MESSAGE, isbn));
    }
}
