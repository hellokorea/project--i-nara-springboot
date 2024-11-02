package com.eureka.mindbloom.event.service.impl;

import com.eureka.mindbloom.common.exception.BadRequestException;
import com.eureka.mindbloom.event.domain.Event;
import com.eureka.mindbloom.event.dto.EventTriggerRequest;
import com.eureka.mindbloom.event.repository.EventRepository;
import com.eureka.mindbloom.event.scheduler.EventEndJob;
import com.eureka.mindbloom.event.scheduler.EventEndJobListener;
import com.eureka.mindbloom.event.scheduler.EventStartJob;
import com.eureka.mindbloom.event.scheduler.EventStartJobListener;
import com.eureka.mindbloom.event.service.EventAdminService;
import lombok.RequiredArgsConstructor;
import org.quartz.*;
import org.quartz.impl.matchers.KeyMatcher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class EventAdminServiceImpl implements EventAdminService {

    private static final int MULTIPLY_PARTICIPANT = 3;
    private final Scheduler scheduler;
    private final EventRepository eventRepository;
    private final StringRedisTemplate eventRedisTemplate;

    @Override
    public void scheduleEvent(EventTriggerRequest request) throws SchedulerException {
        if (request.endTime().isBefore(request.startTime())) {
            throw new BadRequestException("시작 시간은 종료 시간보다 빨라야 합니다.");
        }

        if (request.startTime().isBefore(LocalDateTime.now().plusMinutes(1))) {
            throw new BadRequestException("시작 시간은 최소 1분 후여야 합니다.");
        }

        Event event = eventRepository.save(request.toEventEntity());

        int maxParticipants = request.winnerCount() * MULTIPLY_PARTICIPANT;
        String maxParticipantsKey = "event:maxParticipants";

        JobDetail startJobDetail = JobBuilder.newJob(EventStartJob.class)
                .withIdentity("eventStartJob")
                .usingJobData("eventId", event.getId())
                .usingJobData("maxParticipants", maxParticipants)
                .storeDurably(false)
                .build();

        if (scheduler.checkExists(startJobDetail.getKey())) {
            throw new BadRequestException("이벤트가 이미 스케줄러에 등록되어 있습니다.");
        }

        Objects.requireNonNull(eventRedisTemplate.getConnectionFactory()).getConnection().serverCommands().flushDb();
        eventRedisTemplate.opsForValue().set(maxParticipantsKey, String.valueOf(maxParticipants));

        Trigger startTrigger = TriggerBuilder.newTrigger()
                .forJob(startJobDetail)
                .withIdentity("eventStartTrigger")
                .startAt(convertToDate(event.getStartTime()))
                .build();

        JobDetail endJobDetail = JobBuilder.newJob(EventEndJob.class)
                .withIdentity("eventEndJob")
                .usingJobData("eventId", event.getId())
                .usingJobData("maxParticipants", maxParticipants)
                .storeDurably(false)
                .build();

        Trigger endTrigger = TriggerBuilder.newTrigger()
                .forJob(endJobDetail)
                .withIdentity("eventEndTrigger")
                .startAt(convertToDate(event.getEndTime()))
                .build();

        scheduler.getListenerManager().addJobListener(
                new EventStartJobListener(),
                KeyMatcher.keyEquals(new JobKey("eventStartJob"))
        );

        scheduler.getListenerManager().addJobListener(
                new EventEndJobListener(),
                KeyMatcher.keyEquals(new JobKey("eventEndJob"))
        );

        scheduler.scheduleJob(startJobDetail, startTrigger);
        scheduler.scheduleJob(endJobDetail, endTrigger);
    }

    private Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}
