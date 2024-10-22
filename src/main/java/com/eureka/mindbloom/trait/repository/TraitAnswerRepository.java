package com.eureka.mindbloom.trait.repository;

import com.eureka.mindbloom.trait.domain.survey.TraitAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TraitAnswerRepository extends JpaRepository<TraitAnswer, Integer> {

    @Query(value = """
        SELECT ta
        FROM TraitAnswer ta
        JOIN FETCH ta.question
        WHERE ta.question.id = :questionId
        """)
    List<TraitAnswer> findTraitAnswerByQuestionId(@Param("questionId") int questionId);
}
