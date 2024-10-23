package com.eureka.mindbloom.trait.service;

import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.trait.dto.request.CreateTraitRequest;
import com.eureka.mindbloom.trait.dto.response.QnAResponse;

import java.util.List;

public interface TraitSurveyService {

    List<QnAResponse> getQnA();

    void createTraitByQnA(Member member, List<CreateTraitRequest> answers);
}
