package com.eureka.mindbloom.trait.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@Builder
public class TraitHistoryResponse {

    private String currentTraitValue;
    private Map<String, Integer> traitRecords;
    private List<ActionHistory> actionHistory;
    private List<TraitHistory> changeTraitHistory;

    public TraitHistoryResponse(String currentTraitValue, Map<String, Integer> traitRecords,
                                List<ActionHistory> actionHistory, List<TraitHistory> changeTraitHistory) {

        this.currentTraitValue = currentTraitValue;
        this.traitRecords = traitRecords;
        this.actionHistory = actionHistory;
        this.changeTraitHistory = changeTraitHistory;
    }
}
