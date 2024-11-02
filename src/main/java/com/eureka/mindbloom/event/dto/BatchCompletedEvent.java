package com.eureka.mindbloom.event.dto;

import com.eureka.mindbloom.event.domain.EventParticipant;

import java.util.List;

public record BatchCompletedEvent(String jobName, Integer eventId, List<EventParticipant> winners) {
}