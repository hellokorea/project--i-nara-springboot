package com.eureka.mindbloom.trait.domain.survey;

import com.eureka.mindbloom.common.domain.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChildTraitResponses extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private TraitQuestion question;

    @ManyToOne
    private TraitAnswer answer;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    @Builder
    public ChildTraitResponses(Integer id, TraitQuestion question, TraitAnswer answer, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.id = id;
        this.question = question;
        this.answer = answer;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }
}
