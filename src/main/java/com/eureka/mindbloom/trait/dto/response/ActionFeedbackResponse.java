package com.eureka.mindbloom.trait.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ActionFeedbackResponse {

    private String actionCode;
    private String traitCode;
    private Integer point;

    public ActionFeedbackResponse(String actionCode) {
        this.actionCode = actionCode;
    }
}
