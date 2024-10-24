package com.eureka.mindbloom.trait.dto.response;

import lombok.*;

@Getter
@Builder
public class AdminAnswer {

    private Integer answerId;
    private String content;
    private String traitCode;
    private Integer point;

    public AdminAnswer(Integer answerId, String content, String traitCode, int point) {
        this.answerId = answerId;
        this.content = content;
        this.traitCode = traitCode;
        this.point = point;
    }
}
