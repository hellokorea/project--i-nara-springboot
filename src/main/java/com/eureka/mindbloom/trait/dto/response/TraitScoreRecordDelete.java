package com.eureka.mindbloom.trait.dto.response;

import java.util.List;

public record TraitScoreRecordDelete(
        Long id,
        Long childId,
        String traitCode,
        List<TraitScoreDailyRecordDelete> traitScoreDailyRecord
) {
}