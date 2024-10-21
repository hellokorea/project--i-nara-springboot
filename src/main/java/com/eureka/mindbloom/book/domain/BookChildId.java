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
public class BookChildId implements Serializable {

    private String bookId;
    private Long childId;

    @Builder
    public BookChildId( String bookId, Long childId ) {
        this.bookId = bookId;
        this.childId = childId;

    }
}
