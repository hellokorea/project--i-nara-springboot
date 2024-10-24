package com.eureka.mindbloom.trait.service;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.trait.dto.request.CreateTraitRequest;

import java.util.List;

public interface ChildHistoryRecordService {

    void saveChildResponse(Child child, List<CreateTraitRequest> answers);
}
