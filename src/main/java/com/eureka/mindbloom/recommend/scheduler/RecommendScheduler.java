package com.eureka.mindbloom.recommend.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

@EnableScheduling
@Configuration
@RequiredArgsConstructor
public class RecommendScheduler {
	private final JobLauncher jobLauncher;
	private final Job generateRecommendBooks;

	@Scheduled(cron = "0 0 1 * * ?")
	public void runBatchJob() {
		try {
			JobParameters jobParameters = new JobParametersBuilder()
				.addLong("time", System.currentTimeMillis())
				.toJobParameters();
			jobLauncher.run(generateRecommendBooks, jobParameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
