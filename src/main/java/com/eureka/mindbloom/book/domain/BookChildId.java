package com.eureka.mindbloom.book.domain;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BookChildId implements Serializable {

    private String bookId;
    private Long childId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookChildId bookChildId1 = (BookChildId) o;
        return Objects.equals(getBookId(), bookChildId1.getBookId()) && Objects.equals(getChildId(), bookChildId1.getChildId());
    }

    @Override
    public int hashCode() {
        int result = Objects.hashCode(getBookId());
        result = 31 * result + Objects.hashCode(getChildId());
        return result;
    }

}
