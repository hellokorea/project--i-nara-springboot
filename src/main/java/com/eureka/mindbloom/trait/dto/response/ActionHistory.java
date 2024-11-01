package com.eureka.mindbloom.trait.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ActionHistory {

    private Long historyActionId;
    private String actionCodeName;
    private String traitCodeName;
    private Integer point;
    private LocalDateTime createdAt;

    public ActionHistory(Long historyActionId, String actionCodeName, String traitCodeName, Integer point, LocalDateTime createdAt) {
        this.historyActionId = historyActionId;
        this.actionCodeName = actionCodeName;
        this.traitCodeName = traitCodeName;
        this.point = point;
        this.createdAt = createdAt;
    }
}
