package com.eureka.mindbloom.trait.repository;

import com.eureka.mindbloom.trait.domain.survey.TraitQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TraitQuestionRepository extends JpaRepository<TraitQuestion, Integer> {
}
