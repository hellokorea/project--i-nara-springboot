package com.eureka.mindbloom.trait.service;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.trait.domain.ChildTrait;
import com.eureka.mindbloom.trait.domain.survey.TraitAnswer;
import com.eureka.mindbloom.trait.dto.response.TraitPointsResponse;

import java.util.List;
import java.util.Map;

public interface TraitScoreRecordService {

    List<TraitPointsResponse> saveTraitScoreRecord(ChildTrait childTrait, Map<Integer, TraitAnswer> responseAnswersMap);

    String fetchTraitValue(List<TraitPointsResponse> traitScores);

    Map<String, Integer> getChildTraitScores(Child child);
}
