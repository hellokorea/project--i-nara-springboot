package com.eureka.mindbloom.book.dto;


import com.eureka.mindbloom.book.domain.Book;
import lombok.Getter;

@Getter
public class AdminBookResponseDto {

    private String isbn;
    private String title;
    private String plot;
    private String author;
    private String publisher;
    private String recommendedAge;
    private String coverImage;
    private String keywords;
    private Long viewCount;


    public AdminBookResponseDto(Book book) {
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.plot = book.getPlot();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.recommendedAge = book.getRecommendedAge();
        this.coverImage = book.getCoverImage();
        this.keywords = book.getKeywords();
        this.viewCount = book.getViewCount();
    }
}