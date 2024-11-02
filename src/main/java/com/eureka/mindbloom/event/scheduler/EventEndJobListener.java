package com.eureka.mindbloom.event.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventEndJobListener implements JobListener {

    @Override
    public String getName() {
        return "EndJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info("잡 실행: {}", context.getJobDetail().getKey());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.warn("잡 실행이 거부되었습니다: {}", context.getJobDetail().getKey());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        try {
            log.info("배치 잡 등록 시작");
            Scheduler scheduler = context.getScheduler();

            int eventId = context.getJobDetail().getJobDataMap().getInt("eventId");

            JobDetail participantBatchJob = JobBuilder.newJob(EventParticipantBatchJob.class)
                    .withIdentity("eventParticipantBatchJobDetail")
                    .usingJobData("eventId", eventId)
                    .build();

            Trigger batchJobTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("eventParticipantBatchJobTrigger")
                    .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(3, 0))
                    .build();

            scheduler.scheduleJob(participantBatchJob, batchJobTrigger);
            scheduler.deleteJob(context.getJobDetail().getKey());
            log.info("EventParticipantBatchJob이 새벽 3시에 실행되도록 스케줄링되었습니다. eventId: {}", eventId);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }
}
