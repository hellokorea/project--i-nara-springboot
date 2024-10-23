package com.eureka.mindbloom.trait.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class QnAResponse {

    private Integer questionId;
    private String content;
    private List<Answer> choices;

    public QnAResponse(Integer questionId, String content, List<Answer> choices) {
        this.questionId = questionId;
        this.content = content;
        this.choices = choices;
    }
}
