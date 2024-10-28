package com.eureka.mindbloom.trait.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling
@Configuration
@Slf4j
@RequiredArgsConstructor
public class HardDeleteScheduler {
    private final JobLauncher jobLauncher;
    private final Job hardDeleteJob;

    @Scheduled(cron = "0 0 2 * * ?")
    public void runBatchJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(hardDeleteJob, jobParameters);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }
}