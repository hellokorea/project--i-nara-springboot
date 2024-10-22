package com.eureka.mindbloom.member.service;

import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.dto.ChildRegisterRequest;
import com.eureka.mindbloom.member.dto.ChildRegisterResponse;

public interface ChildService {
    ChildRegisterResponse registerChild(Member parents, ChildRegisterRequest request);
}