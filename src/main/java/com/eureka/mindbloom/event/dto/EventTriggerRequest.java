package com.eureka.mindbloom.event.dto;

import com.eureka.mindbloom.event.domain.Event;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record EventTriggerRequest(
        @NotNull LocalDateTime startTime,
        @NotNull LocalDateTime endTime
) {

    public Event toEventEntity(){
        return new Event(startTime, endTime);
    }
}
