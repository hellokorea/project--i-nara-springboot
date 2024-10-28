package com.eureka.mindbloom.trait.service;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.trait.dto.response.ActionFeedbackResponse;
import com.eureka.mindbloom.trait.dto.response.TraitHistoryResponse;

import java.util.List;

public interface ChildRecordHistoryService {
    List<TraitHistoryResponse> getHistory(Long childId);

    ActionFeedbackResponse createChildTraitHistory(Child child, String actionCode,
                                                   String traitCode, Integer point);

    void getTotalActionHistoryPoint(List<ActionFeedbackResponse> actionFeedbackResponses);
}
