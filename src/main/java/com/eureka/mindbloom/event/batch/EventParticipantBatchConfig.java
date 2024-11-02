package com.eureka.mindbloom.event.batch;

import com.eureka.mindbloom.event.listener.WinnerJobListener;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class EventParticipantBatchConfig {

    private final WinnerJobListener winnerJobListener;
    private final StringRedisTemplate eventRedisTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Bean
    public Tasklet eventParticipantTasklet() {
        return (contribution, chunkContext) -> {
            Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
            Long eventLongId = (Long) jobParameters.get("eventId");
            int eventId = eventLongId.intValue();

            Map<Object, Object> data = eventRedisTemplate.opsForHash().entries("event");

            String sql = """
                    INSERT INTO event_participant (event_id, member_id, event_entry_time, created_at)
                    VALUES (?, ?, ?, now())
                    """;

            jdbcTemplate.batchUpdate(sql, data.entrySet(), 100, (ps, entry) -> {
                ps.setLong(1, eventId);
                ps.setString(2, (String) entry.getKey());
                ps.setString(3, (String) entry.getValue());
            });

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Tasklet backupToCsvTasklet() {
        return (contribution, chunkContext) -> {
            Map<String, Object> jobParameters = chunkContext.getStepContext().getJobParameters();
            Long eventIdLong = (Long) jobParameters.get("eventId");
            Integer eventId = eventIdLong.intValue();

            String fileName = String.format("event_participants_backup_%d.csv", eventId);

            Map<Object, Object> data = eventRedisTemplate.opsForHash().entries("event");

            try (FileWriter writer = new FileWriter(fileName, true)) {
                File file = new File(fileName);
                log.info("파일이 저장될 경로: {}", file.getAbsolutePath());
                writer.write("event_id,member_id,participant_time\n");

                for (Map.Entry<Object, Object> entry : data.entrySet()) {
                    writer.write(eventId + "," + entry.getKey() + "," + entry.getValue() + "\n");
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

            return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public Step eventParticipantStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("eventParticipantStep", jobRepository)
                .tasklet(eventParticipantTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Step backupToCsvStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("backupToCsvStep", jobRepository)
                .tasklet(backupToCsvTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Job eventParticipantJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("eventParticipantJob", jobRepository)
                .start(eventParticipantStep(jobRepository, transactionManager))
                .listener(winnerJobListener)
                .on("FAILED").to(backupToCsvStep(jobRepository, transactionManager))
                .from(eventParticipantStep(jobRepository, transactionManager)).on("*").end()
                .end()
                .build();
    }
}