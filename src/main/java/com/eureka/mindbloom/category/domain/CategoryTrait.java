package com.eureka.mindbloom.category.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CategoryTrait {

    @EmbeddedId
    private CategoryTraitId id;

    private int weight;

    @Builder
    public CategoryTrait(String traitCode, String categoryCode, int weight) {
        this.id = new CategoryTraitId(traitCode, categoryCode);
        this.weight = weight;
    }
}
