package com.eureka.mindbloom.trait.batch;

import com.eureka.mindbloom.trait.dto.response.ChildTraitDelete;
import com.eureka.mindbloom.trait.dto.response.TraitRecordHistoryDelete;
import com.eureka.mindbloom.trait.dto.response.TraitScoreRecordDelete;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class TraitHardDeleteBatchJobConfig {

    private final TaskExecutor batchTaskExecutor;

    @Bean
    public DataSourceTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public Job hardDeleteJob(JobRepository jobRepository, Step childTraitStep, Step traitScoreRecordStep, Step asyncTraitRecordHistoryStep) {
        return new JobBuilder("hardDeleteJob", jobRepository)
                .start(childTraitStep)
                .next(traitScoreRecordStep)
                .next(asyncTraitRecordHistoryStep)
                .build();
    }

    @Bean
    public Step childTraitStep(JobRepository jobRepository, ItemReader<ChildTraitDelete> childTraitReader, ItemWriter<ChildTraitDelete> childTraitWriter, DataSourceTransactionManager transactionManager) {
        return new StepBuilder("childTraitStep", jobRepository)
                .<ChildTraitDelete, ChildTraitDelete>chunk(200, transactionManager)
                .reader(childTraitReader)
                .writer(childTraitWriter)
                .build();
    }

    @Bean
    public ItemReader<ChildTraitDelete> childTraitReader(DataSource dataSource) {
        JdbcPagingItemReader<ChildTraitDelete> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(200);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT id, child_id, trait_group_code");
        queryProvider.setFromClause("FROM child_trait");
        queryProvider.setWhereClause("WHERE deleted_at IS NOT NULL");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);
        reader.setRowMapper(new DataClassRowMapper<>(ChildTraitDelete.class));
        return reader;
    }

    @Bean
    public ItemWriter<ChildTraitDelete> childTraitWriter(DataSource dataSource) {
        return items -> {
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
            String deleteSql = "DELETE FROM child_trait WHERE id = ?";

            List<Long> ids = items.getItems()
                    .stream()
                    .map(ChildTraitDelete::id)
                    .collect(Collectors.toList());

            jdbcTemplate.batchUpdate(deleteSql, ids, ids.size(),
                    (ps, id) -> ps.setLong(1, id));

            Long childId = items.getItems().get(0).childId();
            String responseDelete = "DELETE FROM child_trait_responses WHERE child_id = ? AND deleted_at IS NOT NULL";
            jdbcTemplate.update(responseDelete, childId);
        };
    }


    @Bean
    public Step traitScoreRecordStep(JobRepository jobRepository, ItemReader<TraitScoreRecordDelete> traitScoreRecordReader, ItemWriter<TraitScoreRecordDelete> traitScoreRecordWriter, DataSourceTransactionManager transactionManager) {
        return new StepBuilder("traitScoreRecordStep", jobRepository)
                .<TraitScoreRecordDelete, TraitScoreRecordDelete>chunk(50, transactionManager)
                .reader(traitScoreRecordReader)
                .writer(traitScoreRecordWriter)
                .build();
    }

    @Bean
    public ItemReader<TraitScoreRecordDelete> traitScoreRecordReader(DataSource dataSource) {
        JdbcPagingItemReader<TraitScoreRecordDelete> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setFetchSize(50);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause(
                "SELECT tsr.id AS id, tsr.child_id AS tsr_child_id, tsr.trait_code AS tsr_trait_code, " +
                        "tsdr.id AS tsdr_id, tsdr.trait_code AS tsdr_trait_code"
        );
        queryProvider.setFromClause("FROM trait_score_record tsr LEFT JOIN trait_score_daily_record tsdr ON tsr.id = tsdr.trait_score_record_id");
        queryProvider.setWhereClause("WHERE tsr.deleted_at IS NOT NULL");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("tsr.id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);

        reader.setRowMapper(new TraitScoreRecordDeleteMapper());
        return reader;
    }

    @Bean
    public ItemWriter<TraitScoreRecordDelete> traitScoreRecordWriter(DataSource dataSource) {
        return items -> {
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            List<Long> ids = items.getItems()
                    .stream()
                    .map(TraitScoreRecordDelete::id)
                    .toList();

            String deleteDailyRecordSql = "DELETE FROM trait_score_daily_record WHERE trait_score_record_id IN (:ids)";
            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("ids", ids);
            jdbcTemplate.update(deleteDailyRecordSql, params);

            String deleteRecordSql = "DELETE FROM trait_score_record WHERE id IN (:ids)";
            jdbcTemplate.update(deleteRecordSql, Map.of("ids", ids));
        };
    }

    @Bean
    public Step asyncTraitRecordHistoryStep(JobRepository jobRepository, ItemReader<TraitRecordHistoryDelete> traitRecordHistoryReader, ItemWriter<TraitRecordHistoryDelete> traitRecordHistoryWriter, DataSourceTransactionManager transactionManager) {
        return new StepBuilder("traitRecordHistoryStep", jobRepository)
                .<TraitRecordHistoryDelete, TraitRecordHistoryDelete>chunk(100, transactionManager)
                .reader(traitRecordHistoryReader)
                .writer(traitRecordHistoryWriter)
                .taskExecutor(batchTaskExecutor)
                .build();
    }

    @Bean
    public ItemReader<TraitRecordHistoryDelete> traitRecordHistoryReader(DataSource dataSource) {
        JdbcPagingItemReader<TraitRecordHistoryDelete> reader = new JdbcPagingItemReader<>();
        reader.setDataSource(dataSource);
        reader.setPageSize(100);

        MySqlPagingQueryProvider queryProvider = new MySqlPagingQueryProvider();
        queryProvider.setSelectClause("SELECT id, action_code, trait_code, child_id");
        queryProvider.setFromClause("FROM trait_record_history");
        queryProvider.setWhereClause("WHERE deleted_at IS NOT NULL");

        Map<String, Order> sortKeys = new HashMap<>();
        sortKeys.put("id", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);

        reader.setQueryProvider(queryProvider);
        reader.setRowMapper(new DataClassRowMapper<>(TraitRecordHistoryDelete.class));
        return reader;
    }

    @Bean
    public ItemWriter<TraitRecordHistoryDelete> traitRecordHistoryWriter(DataSource dataSource) {
        return items -> {
            NamedParameterJdbcTemplate jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
            List<Long> ids = items.getItems().stream().map(TraitRecordHistoryDelete::id).toList();

            MapSqlParameterSource params = new MapSqlParameterSource();
            params.addValue("ids", ids);

            String deleteSql = "DELETE FROM trait_record_history WHERE id in (:ids)";
            jdbcTemplate.update(deleteSql, params);
        };
    }
}