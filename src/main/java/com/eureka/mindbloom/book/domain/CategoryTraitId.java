package com.eureka.mindbloom.book.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryTraitId implements Serializable {

    private String traitCode;
    private String categoryCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryTraitId categoryTraitId = (CategoryTraitId) o;
        return Objects.equals(traitCode, categoryTraitId.traitCode);
    }

    @Override
    public int hashCode() {
        int result = traitCode != null ? traitCode.hashCode() : 0;
        result = 31 * result + (categoryCode != null ? categoryCode.hashCode() : 0);
        return result;
    }
}
