package com.eureka.mindbloom.book.dto;

import lombok.Getter;

@Getter
public class BookLikeRequest {

    private final Long childId;
    private final String type;

    public BookLikeRequest(Long childId, String type) {
        this.childId = childId;
        this.type = type;
    }
}
