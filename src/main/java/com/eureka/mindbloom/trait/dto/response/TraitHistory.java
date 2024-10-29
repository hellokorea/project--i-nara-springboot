package com.eureka.mindbloom.trait.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class TraitHistory {

    private Long childTraitId;
    private String beforeTraitValue;
    private LocalDateTime createdAt;

    public TraitHistory(Long childTraitId, String beforeTraitValue, LocalDateTime createdAt) {
        this.childTraitId = childTraitId;
        this.beforeTraitValue = beforeTraitValue;
        this.createdAt = createdAt;
    }
}
