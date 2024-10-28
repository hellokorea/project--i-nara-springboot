package com.eureka.mindbloom.trait.domain.survey;

import com.eureka.mindbloom.common.domain.SoftDeleteEntity;
import com.eureka.mindbloom.member.domain.Child;
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
    private Long id;

    @ManyToOne
    private Child child;

    @ManyToOne(fetch = FetchType.LAZY)
    private TraitQuestion question;

    @ManyToOne
    private TraitAnswer answer;

    @Builder
    public ChildTraitResponses(Long id, Child child, TraitQuestion question, TraitAnswer answer) {
        this.id = id;
        this.child = child;
        this.question = question;
        this.answer = answer;
    }

    public void updateAnswer(TraitAnswer newAnswer) {
        this.answer = newAnswer;
    }
}
