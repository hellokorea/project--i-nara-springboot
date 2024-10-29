package com.eureka.mindbloom.trait.repository;

import com.eureka.mindbloom.trait.domain.history.TraitScoreDailyRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraitScoreDailyRecordRepository extends JpaRepository<TraitScoreDailyRecord, Long> {
}
