package com.eureka.mindbloom.trait.dto.response;

public record ChildTraitDelete(
        Long id,
        Long childId,
        String traitGroupCode
) {
}