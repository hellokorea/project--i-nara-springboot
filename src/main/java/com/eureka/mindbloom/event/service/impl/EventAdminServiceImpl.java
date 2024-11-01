package com.eureka.mindbloom.event.service.impl;

import com.eureka.mindbloom.common.exception.BadRequestException;
import com.eureka.mindbloom.event.domain.Event;
import com.eureka.mindbloom.event.dto.EventTriggerRequest;
import com.eureka.mindbloom.event.repository.EventRepository;
import com.eureka.mindbloom.event.scheduler.EventEndJob;
import com.eureka.mindbloom.event.scheduler.EventStartJob;
import com.eureka.mindbloom.event.service.EventAdminService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@RequiredArgsConstructor
@Transactional
public class EventAdminServiceImpl implements EventAdminService {

    private final Scheduler scheduler;
    private final EventRepository eventRepository;

    @Override
    public void scheduleEvent(EventTriggerRequest request) throws SchedulerException {
        if (request.endTime().isBefore(request.startTime())) {
            throw new BadRequestException("시작 시간은 종료 시간보다 빨라야 합니다.");
        }
        Event event = eventRepository.save(request.toEventEntity());

        JobDetail startJobDetail = JobBuilder.newJob(EventStartJob.class)
                .withIdentity("eventStartJob_" + event.getId())
                .usingJobData("eventId", event.getId())
                .build();

        Trigger startTrigger = TriggerBuilder.newTrigger()
                .forJob(startJobDetail)
                .withIdentity("eventStartTrigger_" + event.getId())
                .startAt(convertToDate(event.getStartTime()))
                .build();

        JobDetail endJobDetail = JobBuilder.newJob(EventEndJob.class)
                .withIdentity("eventEndJob_" + event.getId())
                .usingJobData("eventId", event.getId())
                .build();

        Trigger endTrigger = TriggerBuilder.newTrigger()
                .forJob(endJobDetail)
                .withIdentity("eventEndTrigger_" + event.getId())
                .startAt(convertToDate(event.getEndTime()))
                .build();

        scheduler.scheduleJob(startJobDetail, startTrigger);
        scheduler.scheduleJob(endJobDetail, endTrigger);
    }

    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}