package com.eureka.mindbloom.event.scheduler;

import com.eureka.mindbloom.event.service.EventService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

@Component
public class EventStartJob implements Job {

    private final EventService eventService;

    public EventStartJob(EventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public void execute(JobExecutionContext context) {
        Integer eventId = context.getJobDetail().getJobDataMap().getInt("eventId");
        eventService.startEvent(eventId);
    }
}
