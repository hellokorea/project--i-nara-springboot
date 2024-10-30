package com.eureka.mindbloom.trait.service;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.trait.dto.response.ActionFeedbackResponse;
import com.eureka.mindbloom.trait.dto.response.TraitHistoryResponse;

public interface ChildRecordHistoryService {
    TraitHistoryResponse getHistory(Long childId);

    ActionFeedbackResponse createChildTraitHistory(Child child, String actionCode,
                                                   String traitCode, Integer point);

    ActionFeedbackResponse testPost(Long childId);
}
