package com.eureka.mindbloom.event.service;

import com.eureka.mindbloom.event.dto.EventTriggerRequest;
import org.quartz.SchedulerException;

public interface EventAdminService {
    void scheduleEvent(EventTriggerRequest request) throws SchedulerException;

}
