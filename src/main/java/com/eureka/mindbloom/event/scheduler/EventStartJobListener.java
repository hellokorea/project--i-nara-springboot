package com.eureka.mindbloom.event.scheduler;

import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventStartJobListener implements JobListener {

    @Override
    public String getName() {
        return "StartJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        log.info("인터럽트 잡 실행: {}", context.getJobDetail().getKey());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {
        log.warn("인터럽트 잡 실행이 거부되었습니다: {}", context.getJobDetail().getKey());
    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        try {
            Scheduler scheduler = context.getScheduler();
            JobDetail interruptJobDetail = JobBuilder.newJob(EventInterruptJob.class)
                    .withIdentity("eventInterruptJob")
                    .usingJobData("maxParticipants", context.getJobDetail().getJobDataMap().getInt("maxParticipants"))
                    .build();

            Trigger interruptTrigger = TriggerBuilder.newTrigger()
                    .withIdentity("eventInterruptTrigger")
                    .startNow()
                    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                            .withIntervalInSeconds(20)
                            .repeatForever())
                    .build();

            scheduler.scheduleJob(interruptJobDetail, interruptTrigger);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
