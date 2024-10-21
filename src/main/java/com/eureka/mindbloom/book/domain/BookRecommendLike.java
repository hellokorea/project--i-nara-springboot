package com.eureka.mindbloom.book.domain;

import com.eureka.mindbloom.common.domain.BaseEntity;
import com.eureka.mindbloom.member.domain.Child;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookRecommendLike extends BaseEntity {

    @EmbeddedId
    private RecommendLikeId id;

    @MapsId("bookRecommendId")
    @ManyToOne
    @JoinColumn(name = "book_recommend_id")
    private BookRecommend bookRecommend;

    @MapsId("childId")
    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;
}
