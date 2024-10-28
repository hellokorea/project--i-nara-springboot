package com.eureka.mindbloom.trait.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class ActionFeedbackResponse {

    private String traitCode;
    private Integer point;

    public ActionFeedbackResponse(String traitCode, Integer point) {
        this.traitCode = traitCode;
        this.point = point;
    }
}
