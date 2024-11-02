package com.eureka.mindbloom.event.scheduler;

import com.eureka.mindbloom.common.exception.BaseException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class EventInterruptJob implements Job {

    private final StringRedisTemplate eventRedisTemplate;
    private final Scheduler scheduler;

    @Override
    public void execute(JobExecutionContext context) {
        log.info("EventInterruptJob started");
        String eventKey = "event";
        int maxParticipants = context.getJobDetail().getJobDataMap().getInt("maxParticipants");

        Long currentParticipants = eventRedisTemplate.opsForHash().size(eventKey);

        try {
            if (!scheduler.checkExists(new JobKey("eventEndJob"))) {
                log.info("EventEndJob has already been executed. EventInterruptJob will be terminated.");
                scheduler.deleteJob(context.getJobDetail().getKey());
                return;
            }

            if (currentParticipants >= maxParticipants) {
                log.info("스케줄러를 종료합니다. 이벤트 참가자 수가 충분합니다.");
                scheduler.triggerJob(new JobKey("eventEndJob"));
                scheduler.deleteJob(context.getJobDetail().getKey());
            }
        } catch (SchedulerException e) {
            throw new BaseException("스케줄러 종료 중 오류가 발생했습니다.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
