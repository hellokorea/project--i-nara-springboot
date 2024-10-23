package com.eureka.mindbloom.book.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BookLikeResponse {

    private final String bookId;
    private final Long childId;
    private final String type;
    private final String likedAt;

    @Builder
    public BookLikeResponse(String bookId, Long childId, String type, String likedAt) {
        this.bookId = bookId;
        this.childId = childId;
        this.type = type;
        this.likedAt = likedAt;
    }
}
