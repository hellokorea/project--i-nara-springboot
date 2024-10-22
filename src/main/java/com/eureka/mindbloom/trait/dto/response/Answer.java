package com.eureka.mindbloom.trait.dto.response;

import lombok.*;

@Getter
@Builder
public class Answer {

    private Integer answerId;
    private String content;
    private String traitCode;
    private Integer point;

    public Answer(Integer answerId, String content, String traitCode, int point) {
        this.answerId = answerId;
        this.content = content;
        this.traitCode = traitCode;
        this.point = point;
    }
}
