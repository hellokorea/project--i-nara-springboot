package com.eureka.mindbloom.book.dto;

import lombok.Data;

@Data
public class ReadBookResponse {

    private String isbn;
    private String title;

    public ReadBookResponse(String isbn, String title) {
        this.isbn = isbn;
        this.title = title;
    }
}
