package com.eureka.mindbloom.common.exception;

import org.springframework.http.HttpStatus;

public class NotFoundException extends BaseException {
    private static final String CHILD = "자녀 정보가 존재하지 않습니다. %s";
    private static final String BOOK = "도서 정보가 존재하지 않습니다. %s";
    private static final String BOOK_LIKE = "도서 '%s'에 대한 좋아요 정보가 존재하지 않습니다.";


    protected NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }

    public static NotFoundException childNotFound(Long childId) {
        return new NotFoundException(String.format(CHILD, childId));
    }

    public static NotFoundException bookNotFound(String isbn) {
        return new NotFoundException(String.format(BOOK, isbn));
    }

    public static NotFoundException bookLikeNotFound(String isbn) {
        return new NotFoundException(String.format(BOOK_LIKE, isbn));
    }
}
