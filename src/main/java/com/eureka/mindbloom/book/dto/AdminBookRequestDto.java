package com.eureka.mindbloom.book.dto;


import com.eureka.mindbloom.book.domain.Book;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
//등록수정
public class AdminBookRequestDto {

    private String isbn;
    private String title;
    private String plot;
    private String author;
    private String publisher;
    private String recommendedAge;
    private String coverImage;
    private String keywords;
    private Long viewCount;


    @Builder
    public AdminBookRequestDto(String isbn, String title, String plot, String author, String publisher, String recommendedAge, String coverImage, String keywords, Long viewCount) {
        this.isbn = isbn;
        this.title = title;
        this.plot = plot;
        this.author = author;
        this.publisher = publisher;
        this.recommendedAge = recommendedAge;
        this.coverImage = coverImage;
        this.keywords = keywords;
        this.viewCount = viewCount;
    }


    public Book toEntity() {
        return Book.builder()
                .isbn(this.isbn)
                .title(this.title)
                .plot(this.plot)
                .author(this.author)
                .publisher(this.publisher)
                .recommendedAge(this.recommendedAge)
                .coverImage(this.coverImage)
                .keywords(this.keywords)
                .viewCount(this.viewCount)
                .build();
    }
}