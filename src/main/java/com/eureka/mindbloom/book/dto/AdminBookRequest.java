package com.eureka.mindbloom.book.dto;

import com.eureka.mindbloom.book.domain.Book;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@NoArgsConstructor
public class AdminBookRequest {

    private String isbn;
    private String title;
    private String plot;
    private String author;
    private String publisher;
    private String recommendedAge;
    private String coverImage;
    private String keywords;
    private Long viewCount;  // 여기서 int를 Long으로 변경
    private List<AdminBookCategory> categories;

    @Builder
    public AdminBookRequest(String isbn, String title, String plot, String author, String publisher,
                            String recommendedAge, String coverImage, String keywords, Long viewCount,  // 수정된 부분
                            List<AdminBookCategory> categories) {
        this.isbn = isbn;
        this.title = title;
        this.plot = plot;
        this.author = author;
        this.publisher = publisher;
        this.recommendedAge = recommendedAge;
        this.coverImage = coverImage;
        this.keywords = keywords;
        this.viewCount = viewCount;
        this.categories = categories;
    }

    // AdminBookRequestDto를 Book 엔티티로 변환하는 메서드
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
                .viewCount(this.viewCount)  // 수정된 부분
                .build();
    }

    // 카테고리 목록을 String 형태로 반환하는 메서드
    public List<String> getCategoriesAsStrings() {
        return categories.stream().map(AdminBookCategory::getCategoryName).collect(Collectors.toList());
    }
}
