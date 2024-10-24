package com.eureka.mindbloom.book.dto;

import lombok.*;

@Data
public class BooksResponse {
    String isbn;
    String title;
    String author;
    String coverImage;

    public BooksResponse(String isbn, String title, String author, String coverImage) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.coverImage = coverImage;
    }
}
