package com.eureka.mindbloom.book.domain;

import com.eureka.mindbloom.common.domain.SoftDeleteEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    private String coverImage;

    private String keywords;

    private Long viewCount;
}
