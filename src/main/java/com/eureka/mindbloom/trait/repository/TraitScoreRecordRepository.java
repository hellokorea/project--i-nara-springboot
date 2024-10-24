package com.eureka.mindbloom.trait.repository;

import com.eureka.mindbloom.trait.domain.history.TraitScoreRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TraitScoreRecordRepository extends JpaRepository<TraitScoreRecord, Long> {

    @Query(value = """
        SELECT tsr
        FROM TraitScoreRecord tsr
        WHERE tsr.child.id = :childId
        AND tsr.deletedAt IS NULL
    """)
    List<TraitScoreRecord> findByChildAndDeletedAtIsNull(@Param("childId") Long childId);
}
