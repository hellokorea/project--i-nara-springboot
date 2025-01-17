package com.eureka.mindbloom.event.scheduler;

import com.eureka.mindbloom.event.service.EventService;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EventEndJob implements Job {

    private final EventService eventService;

    public EventEndJob(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void execute(JobExecutionContext context) {
        log.info("EventEndJob started");
        Integer eventId = context.getJobDetail().getJobDataMap().getInt("eventId");
        eventService.endEvent(eventId);
    }
}