package com.eureka.mindbloom.trait.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
public class TraitValueResultResponse {

    private String traitValue;
    Map<String, Integer> valueData;

    public TraitValueResultResponse(String traitValue, Map<String, Integer> valueData) {
        this.traitValue = traitValue;
        this.valueData = valueData;
    }
}
