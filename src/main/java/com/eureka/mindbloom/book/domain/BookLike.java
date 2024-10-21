package com.eureka.mindbloom.book.domain;

import com.eureka.mindbloom.common.domain.BaseEntity;
import com.eureka.mindbloom.common.domain.code.CommonCodeId;
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
}
