package com.eureka.mindbloom.trait.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdminQnADeleteResponse {

    private Integer questionId;
    private List<Integer> answerIds;

    public AdminQnADeleteResponse(Integer questionId, List<Integer> answerIds) {
        this.questionId = questionId;
        this.answerIds = answerIds;
    }
}
