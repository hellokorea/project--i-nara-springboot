package com.eureka.mindbloom.book.domain;

import com.eureka.mindbloom.common.domain.BaseEntity;
import com.eureka.mindbloom.common.domain.code.CommonCodeId;
import com.eureka.mindbloom.member.domain.Child;
import jakarta.persistence.*;
import lombok.AccessLevel;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookLike extends BaseEntity {

    @EmbeddedId
    private BookChildId bookChildId;


    private CommonCodeId type;

    @MapsId("bookId")
    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @MapsId("childId")
    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

    @Builder
    public BookLike( CommonCodeId type , Book book , Child child ) {
        this.bookChildId = BookChildId.builder().bookId(book.getIsbn()).childId(child.getId()).build();
        this.type = type;
        this.book = book;
        this.child = child;

    }
}
