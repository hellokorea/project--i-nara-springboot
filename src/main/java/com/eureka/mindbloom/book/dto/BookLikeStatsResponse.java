package com.eureka.mindbloom.book.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class BookLikeStatsResponse {
    private String isbn;
    private String type;
    private Long count;

    @Builder
    public BookLikeStatsResponse(String isbn, String type, Long count) {
        this.isbn = isbn;
        this.type = type;
        this.count = count;
    }
}
