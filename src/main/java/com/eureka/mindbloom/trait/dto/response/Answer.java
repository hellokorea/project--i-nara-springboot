package com.eureka.mindbloom.trait.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Answer {

    private Integer answerId;
    private String content;

    public Answer(Integer answerId, String content) {
        this.answerId = answerId;
        this.content = content;
    }
}
