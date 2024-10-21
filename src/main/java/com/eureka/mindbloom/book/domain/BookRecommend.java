package com.eureka.mindbloom.book.domain;

import com.eureka.mindbloom.common.domain.BaseEntity;
import com.eureka.mindbloom.member.domain.Child;
import jakarta.persistence.*;
import lombok.AccessLevel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookRecommend extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

    @Builder
    public BookRecommend( Book book , Child child ) {
        this.book = book;
        this.child = child;

    }
}
