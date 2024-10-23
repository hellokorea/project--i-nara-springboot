package com.eureka.mindbloom.trait.dto.request;

import com.eureka.mindbloom.trait.dto.response.Answer;
import lombok.Getter;

import java.util.List;

@Getter
public class AdminQnARequest {

    private String questionContent;
    private boolean disabled;
    private List<Answer> choices;
}
