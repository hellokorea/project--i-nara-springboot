package com.eureka.mindbloom.trait.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CreateTraitRequest {

    private Integer questionId;
    private Integer answerId;
}
