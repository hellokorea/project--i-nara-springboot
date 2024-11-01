package com.eureka.mindbloom.event.service;

import com.eureka.mindbloom.member.domain.Member;

public interface EventService {
    String submitEventEntry(Member member);
    void endEvent(Integer eventId);
    void startEvent(Integer eventId);
}