package com.eureka.mindbloom.book.domain;

import com.eureka.mindbloom.category.CategoryTrait;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookCategory {

    @EmbeddedId
    private BookCategoryId id;

    @MapsId("categoryTraitId")
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "trait_code"),
            @JoinColumn(name = "category_code")
    })
    private CategoryTrait categoryTrait;

    @MapsId("isbn")
    @ManyToOne
    @JoinColumn(name = "isbn")
    private Book book;

    public BookCategory(CategoryTrait categoryTrait, Book book) {
        this.categoryTrait = categoryTrait;
        this.book = book;
        this.id = new BookCategoryId(categoryTrait.getId(), book.getIsbn());
    }
}
