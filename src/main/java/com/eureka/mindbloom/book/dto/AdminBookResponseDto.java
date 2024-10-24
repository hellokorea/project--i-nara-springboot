package com.eureka.mindbloom.book.dto;

import com.eureka.mindbloom.book.domain.Book;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<String> categories; // 카테고리 목록
    private List<String> traits; // 해당 카테고리에 맞는 trait 목록

    public AdminBookResponseDto(Book book, List<String> categories, List<String> traits) {
        this.isbn = book.getIsbn();
        this.title = book.getTitle();
        this.plot = book.getPlot();
        this.author = book.getAuthor();
        this.publisher = book.getPublisher();
        this.recommendedAge = book.getRecommendedAge();
        this.coverImage = book.getCoverImage();
        this.keywords = book.getKeywords();
        this.viewCount = book.getViewCount();
        this.categories = categories;
        this.traits = traits;
    }

    public AdminBookResponseDto(Book book, List<String> categories) {
        this(book, categories, categories.stream().map(c -> "").collect(Collectors.toList())); // 초기화, trait 없는 경우
    }
}
