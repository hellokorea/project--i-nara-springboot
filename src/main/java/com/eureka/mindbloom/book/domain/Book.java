package com.eureka.mindbloom.book.domain;

import com.eureka.mindbloom.common.domain.SoftDeleteEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@AllArgsConstructor
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
}
