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
@Builder
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

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public ChildTraitResponses(Long id, Child child, TraitQuestion question, TraitAnswer answer, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.id = id;
        this.child = child;
        this.question = question;
        this.answer = answer;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public void updateAnswer(TraitAnswer newAnswer) {
        this.answer = newAnswer;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
