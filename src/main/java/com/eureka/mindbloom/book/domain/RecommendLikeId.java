package com.eureka.mindbloom.book.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendLikeId implements Serializable {

    private Long bookRecommendId;
    private Long childId;

    @Builder
    public RecommendLikeId( Long bookRecommendId, Long childId ) {
        this.bookRecommendId = bookRecommendId;
        this.childId = childId;

    }
}
