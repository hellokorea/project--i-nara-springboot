package com.eureka.mindbloom.event.listener;

import com.eureka.mindbloom.event.domain.EventParticipant;
import com.eureka.mindbloom.event.dto.BatchCompletedEvent;
import com.eureka.mindbloom.event.repository.EventParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.JobParameters;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WinnerJobListener implements JobExecutionListener {

    private final EventParticipantRepository eventParticipantRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus().isUnsuccessful()) {
            return;
        }

        JobParameters jobParameters = jobExecution.getJobParameters();
        Long eventLongId = jobParameters.getLong("eventId");
        int eventId = Objects.requireNonNull(eventLongId).intValue();
        Long winnerCountLong = jobParameters.getLong("maxParticipants");
        int winnerCount = Objects.requireNonNull(winnerCountLong).intValue() / 3;

        Pageable page = PageRequest.of(0, winnerCount);

        List<EventParticipant> winners = eventParticipantRepository.findByEventIdOrderByValue(eventId, page);

        eventPublisher.publishEvent(new BatchCompletedEvent(jobExecution.getJobInstance().getJobName(), eventId, winners));
    }
}