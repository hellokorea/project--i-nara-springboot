package com.eureka.mindbloom.book.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

import com.eureka.mindbloom.category.domain.CategoryTraitId;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookCategoryId implements Serializable {

    private CategoryTraitId categoryTraitId;
    private String isbn;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookCategoryId that = (BookCategoryId) o;
        return Objects.equals(categoryTraitId, that.categoryTraitId) && Objects.equals(isbn, that.isbn);
    }

    @Override
    public int hashCode() {
        int result = categoryTraitId != null ? categoryTraitId.hashCode() : 0;
        result = 31 * result + (isbn != null ? isbn.hashCode() : 0);
        return result;
    }
}
