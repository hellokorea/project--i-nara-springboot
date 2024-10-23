package com.eureka.mindbloom.member.service;

import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.dto.ChildProfileResponse;
import com.eureka.mindbloom.member.dto.ChildRegisterRequest;
import com.eureka.mindbloom.member.dto.ChildRegisterResponse;
import com.eureka.mindbloom.member.dto.UpdateChildRequest;

import java.util.List;

public interface ChildService {
    ChildRegisterResponse registerChild(Member parents, ChildRegisterRequest request);
    List<ChildProfileResponse> getChildProfile(Member parents);
    void updateChildProfile(Member member, Long childId, UpdateChildRequest request);
}