package com.eureka.mindbloom.book.domain;

import com.eureka.mindbloom.common.domain.SoftDeleteEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Book extends SoftDeleteEntity {

    @Id
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
    public Book( String isbn , String title , String plot , String author , String publisher , String recommendedAge , String coverImage , String keywords , Long viewCount ) {
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
}
