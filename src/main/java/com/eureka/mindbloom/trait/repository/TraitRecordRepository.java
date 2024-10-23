package com.eureka.mindbloom.trait.repository;

import com.eureka.mindbloom.trait.domain.history.TraitRecord;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraitRecordRepository extends JpaRepository<TraitRecord, Long> {
}
