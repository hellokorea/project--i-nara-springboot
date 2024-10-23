package com.eureka.mindbloom.trait.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AdminQnAResponse {

    private Integer questionId;
    private String traitCodeGroup;
    private String content;
    private boolean disabled;
    private List<Answer> choices;

    public AdminQnAResponse(Integer questionId, String traitCodeGroup, String content, boolean disabled, List<Answer> choices) {
        this.questionId = questionId;
        this.traitCodeGroup = traitCodeGroup;
        this.content = content;
        this.disabled = disabled;
        this.choices = choices;
    }
}
