package com.eureka.mindbloom.trait.service;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.trait.domain.survey.TraitAnswer;
import com.eureka.mindbloom.trait.dto.request.CreateTraitRequest;

import java.util.List;
import java.util.Map;

public interface ChildTraitResponseService {

    Map<Integer, TraitAnswer> saveChildResponse(Child child, List<CreateTraitRequest> answers);

    void softDeleteChildResponse(Long childId);
}
