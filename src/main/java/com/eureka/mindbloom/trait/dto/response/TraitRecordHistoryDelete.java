package com.eureka.mindbloom.trait.dto.response;

public record TraitRecordHistoryDelete(
        Long id,
        String actionCode,
        String traitCode,
        Long childId
) {
}