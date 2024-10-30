package com.eureka.mindbloom.book.domain;

import com.eureka.mindbloom.common.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "book_like_stats")
public class BookLikeStats extends BaseEntity {

    @EmbeddedId
    private BookLikeStatsId id;

    @MapsId("isbn")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "isbn")
    private Book book;

    @Column(name = "count")
    private Long count;

    @Builder
    public BookLikeStats(Book book, String type, Long count) {
        this.id = new BookLikeStatsId(book.getIsbn(), type);
        this.book = book;
        this.count = count;
    }

    public void updateCount(long newCount) {
        this.count = newCount;
    }
}
