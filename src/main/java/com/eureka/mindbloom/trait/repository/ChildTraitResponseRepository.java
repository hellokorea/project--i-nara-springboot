package com.eureka.mindbloom.trait.repository;

import com.eureka.mindbloom.trait.domain.survey.ChildTraitResponses;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChildTraitResponseRepository extends JpaRepository<ChildTraitResponses, Integer> {

    @Query(value = """
        SELECT ctr
        FROM  ChildTraitResponses ctr
        WHERE ctr.child.id = :childId AND ctr.question.id IN :questionIds
        """)
    List<ChildTraitResponses> findByChildIdAndQuestionIds(@Param("childId") Long childId,
                                                          @Param("questionIds") List<Integer> questionIds);
}
