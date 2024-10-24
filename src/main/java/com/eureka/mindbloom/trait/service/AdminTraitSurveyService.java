package com.eureka.mindbloom.trait.service;

import com.eureka.mindbloom.trait.dto.request.AdminQnARequest;
import com.eureka.mindbloom.trait.dto.response.AdminQnADeleteResponse;
import com.eureka.mindbloom.trait.dto.response.AdminQnAResponse;

import java.util.List;

public interface AdminTraitSurveyService {

    List<AdminQnAResponse> getQnA();

    AdminQnAResponse createQnA(AdminQnARequest adminQnARequest);

    AdminQnAResponse updateQnA(Integer questionId, AdminQnARequest adminQnARequest);

    AdminQnADeleteResponse deleteQnA(Integer questionId);
}
