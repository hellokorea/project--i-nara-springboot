package com.eureka.mindbloom.event.controller;

import com.eureka.mindbloom.common.dto.ApiResponse;
import com.eureka.mindbloom.event.dto.EventTriggerRequest;
import com.eureka.mindbloom.event.service.EventAdminService;
import com.eureka.mindbloom.event.service.EventService;
import com.eureka.mindbloom.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.quartz.SchedulerException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;
    private final EventAdminService eventAdminService;

    @PostMapping("/event/issue-ticket")
    public ResponseEntity<?> issueTicket(@AuthenticationPrincipal(expression = "member") Member member) {

        return ResponseEntity.ok(ApiResponse.success(eventService.submitEventEntry(member)));
    }

    @PostMapping("/admin/event")
    public ResponseEntity<?> registerEventTrigger(@RequestBody EventTriggerRequest request) throws SchedulerException {

        eventAdminService.scheduleEvent(request);
        return ResponseEntity.ok(ApiResponse.success("OK"));
    }
}