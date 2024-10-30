package com.eureka.mindbloom.trait.service;

import com.eureka.mindbloom.trait.dto.response.ActionFeedbackResponse;

public interface TraitLikePointService {
    ActionFeedbackResponse processLikePoint(String isbn, Long childId);
}
