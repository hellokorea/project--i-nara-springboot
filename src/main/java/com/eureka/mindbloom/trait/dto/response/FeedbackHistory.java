package com.eureka.mindbloom.trait.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FeedbackHistory {

    private Long id;
    private String actionCode;
    private String traitCode;
    private String bookName;
    private Integer point;
    private LocalDateTime createdAt;


    public FeedbackHistory(Long id, String actionCode, String traitCode, String bookName, Integer point, LocalDateTime createdAt) {
        this.id = id;
        this.actionCode = actionCode;
        this.traitCode = traitCode;
        this.bookName = bookName;
        this.point = point;
        this.createdAt = createdAt;
    }
}


