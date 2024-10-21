package com.eureka.mindbloom.book.domain;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecommendLikeId implements Serializable {

    private Long bookRecommendId;
    private Long childId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RecommendLikeId recommendLikeId = (RecommendLikeId) o;

        if (bookRecommendId != null ? !bookRecommendId.equals(recommendLikeId.bookRecommendId) : recommendLikeId.bookRecommendId != null)
            return false;
        return childId != null ? childId.equals(recommendLikeId.childId) : recommendLikeId.childId == null;
    }

    @Override
    public int hashCode() {
        int result = bookRecommendId != null ? bookRecommendId.hashCode() : 0;
        result = 31 * result + (childId != null ? childId.hashCode() : 0);
        return result;
    }
}
