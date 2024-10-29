package com.eureka.mindbloom.trait.batch;

import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.repository.ChildRepository;
import com.eureka.mindbloom.trait.dto.response.UpdateTraitBatchResponse;
import com.eureka.mindbloom.trait.service.TraitScoreDailyRecordService;
import com.eureka.mindbloom.trait.service.TraitScoreRecordService;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Sort;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Configuration
@EnableBatchProcessing
@RequiredArgsConstructor
public class TraitScoreRecordBatchConfig {

    private final JobRepository jobRepository;
    private final TraitScoreRecordService traitScoreRecordService;
    private final ChildRepository childRepository;
    private final TraitScoreDailyRecordService traitScoreDailyRecordService;

    @Primary
    @Bean(name = "traitScoreTransactionManager")
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public Job traitScoreRecordJob(Step traitScoreRecordHistoryStep) {
        return new JobBuilder("traitScoreRecordJob", jobRepository)
                .start(traitScoreRecordHistoryStep)
                .build();
    }

    @Bean
    public Step traitScoreRecordHistoryStep(@Qualifier("traitScoreTransactionManager") PlatformTransactionManager transactionManager) {
        return new StepBuilder("traitScoreRecordHistoryStep", jobRepository)
                .<Child, UpdateTraitBatchResponse>chunk(1000, transactionManager)
                .reader(childItemReader())
                .processor(traitScoreProcessor())
                .writer(traitWriterDailyScore())
                .faultTolerant()
                .retryLimit(2)
                .retry(Exception.class)
                .build();
    }

    @Bean
    public ItemReader<Child> childItemReader() {
        return new RepositoryItemReaderBuilder<Child>()
                .name("childReader")
                .repository(childRepository)
                .methodName("findAll")
                .sorts(Collections.singletonMap("id", Sort.Direction.ASC))
                .build();
    }

    @Bean
    public ItemProcessor<Child, UpdateTraitBatchResponse> traitScoreProcessor() {
        return child -> {
            Map<String, Integer> dailyTraitTotalPoints = traitScoreRecordService.calculateDailyTraitPoints(child);
            UpdateTraitBatchResponse response = traitScoreRecordService.updateTraitPointsBatch(child, dailyTraitTotalPoints);
            log.info("일일 집계 배치 로직 시작 ->, Processed child ID: {}, response: {}", child.getId(), response);
            return response;
        };
    }

    @Bean
    public ItemWriter<UpdateTraitBatchResponse> traitWriterDailyScore() {
        return responses -> {
            for (UpdateTraitBatchResponse response : responses) {
                traitScoreDailyRecordService.createDailyHistoryRecord(response.getGroupedCodes(), response.getDailyTraitTotalPoints());
                traitScoreRecordService.createNewChildTraitValue(response.getChild(), response.getNewChildTraitValue());
                log.info("유저 MBTI 일일 집계 배치 성공 !! ->, child ID: {}", response.getChild().getId());
            }
        };
    }
}
