package com.eureka.mindbloom.trait.repository;

import com.eureka.mindbloom.trait.domain.history.TraitRecordHistory;
import com.eureka.mindbloom.trait.dto.response.ActionFeedbackResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TraitRecordHistoryRepository extends JpaRepository<TraitRecordHistory, Long> {

    @Query(value = """
        SELECT new com.eureka.mindbloom.trait.dto.response.ActionFeedbackResponse(trh.traitCode, trh.point)
        FROM TraitRecordHistory trh
        WHERE trh.child.id = :childId
        AND trh.deletedAt IS NULL
        """)
    List<ActionFeedbackResponse> getChildActionHistoryDeletedAtIsNull(@Param("childId") Long childId);
}
