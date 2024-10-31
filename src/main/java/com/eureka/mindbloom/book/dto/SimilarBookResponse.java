package com.eureka.mindbloom.book.dto;

import lombok.Data;

@Data
public class SimilarBookResponse {

    private String isbn;
    private String title;
    private String coverImage;

    public SimilarBookResponse(String isbn, String title, String coverImage) {
        this.isbn = isbn;
        this.title = title;
        this.coverImage = coverImage;
    }
}
