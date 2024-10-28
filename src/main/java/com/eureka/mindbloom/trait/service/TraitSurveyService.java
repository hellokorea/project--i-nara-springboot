package com.eureka.mindbloom.trait.service;

import com.eureka.mindbloom.trait.dto.request.CreateTraitRequest;
import com.eureka.mindbloom.trait.dto.response.QnAResponse;

import java.util.List;

public interface TraitSurveyService {

    List<QnAResponse> getQnA(Long childId);

    void createTraitByQnA(Long childId, List<CreateTraitRequest> answers);
}
