package com.eureka.mindbloom.book.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

// TODO: 좋아요 수 기능 구현시 BookLikeCount 삭제
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookLikeCount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;

    private int count;
}
