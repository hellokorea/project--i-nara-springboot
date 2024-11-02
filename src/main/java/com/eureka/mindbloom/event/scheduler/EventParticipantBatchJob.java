package com.eureka.mindbloom.event.scheduler;

import com.eureka.mindbloom.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventParticipantBatchJob implements Job {

    private final JobLauncher jobLauncher;
    private final org.springframework.batch.core.Job eventParticipantJob;

    @Override
    public void execute(JobExecutionContext context) {
        try {
            log.info("Starting event participant job");
            int eventId = context.getJobDetail().getJobDataMap().getInt("eventId");

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("eventId", (long) eventId)
                    .toJobParameters();

            jobLauncher.run(eventParticipantJob, jobParameters);
        } catch (JobInstanceAlreadyCompleteException | JobExecutionAlreadyRunningException |
                 JobParametersInvalidException | JobRestartException e) {
            throw new BaseException(e.getMessage(), e, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}