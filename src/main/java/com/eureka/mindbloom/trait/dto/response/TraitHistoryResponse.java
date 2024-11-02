package com.eureka.mindbloom.trait.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class TraitHistoryResponse {

    private Long childTraitId;
    private String currentTraitValue;
    private LocalDateTime currentTraitValueCreatedAt;
    private Map<String, Integer> traitRecords;
    private List<ActionHistory> actionHistory;
    private List<TraitHistory> changeTraitHistory;

    public TraitHistoryResponse(Long childTraitId, String currentTraitValue, LocalDateTime currentTraitValueCreatedAt,
                                Map<String, Integer> traitRecords, List<ActionHistory> actionHistory, List<TraitHistory> changeTraitHistory) {

        this.childTraitId = childTraitId;
        this.currentTraitValue = currentTraitValue;
        this.currentTraitValueCreatedAt = currentTraitValueCreatedAt;
        this.traitRecords = traitRecords;
        this.actionHistory = actionHistory;
        this.changeTraitHistory = changeTraitHistory;
    }
}
