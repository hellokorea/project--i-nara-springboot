package com.eureka.mindbloom.book.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BookDetailResponse {

    private String isbn;
    private String title;
    private String author;
    private String plot;
    private String publisher;
    private String recommededAge;
    private String coverImage;
    private String category;
    private String keyword;
    private String viewCount;
    private String likeCount;
    private String createdAt;

    public BookDetailResponse(String isbn, String title, String author, String plot, String publisher, String recommendedAge, String coverImage, String categoryName, String keywords, Long viewCount, Long likeCountByIsbn, LocalDateTime createdAt) {
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.plot = plot;
        this.publisher = publisher;
        this.recommededAge = recommendedAge;
        this.coverImage = coverImage;
        this.category = categoryName;
        this.keyword = keywords;
        this.viewCount = String.valueOf(viewCount);
        this.likeCount = String.valueOf(likeCountByIsbn);
        this.createdAt = createdAt.toString();
    }
}
