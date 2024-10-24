package com.eureka.mindbloom.trait.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TraitPointsResponse {

    private String traitCode;
    private Integer traitScore;
}
