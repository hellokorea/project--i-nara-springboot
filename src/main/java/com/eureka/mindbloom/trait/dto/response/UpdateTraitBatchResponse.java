package com.eureka.mindbloom.trait.dto.response;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.trait.domain.history.TraitScoreRecord;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class UpdateTraitBatchResponse {

    private Child child;
    Map<String, List<TraitScoreRecord>> groupedCodes;
    private Map<String, Integer> dailyTraitTotalPoints;
    private String newChildTraitValue;

    public UpdateTraitBatchResponse(Child child,
                                    Map<String, List<TraitScoreRecord>> groupedCodes,
                                    Map<String, Integer> dailyTraitTotalPoints,
                                    String newChildTraitValue) {

        this.child = child;
        this.groupedCodes = groupedCodes;
        this.dailyTraitTotalPoints = dailyTraitTotalPoints;
        this.newChildTraitValue = newChildTraitValue;
    }
}
