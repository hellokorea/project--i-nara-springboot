package com.eureka.mindbloom.trait.domain.survey;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TraitAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private TraitQuestion question;

    private String content;

    private String traitCode;

    private Integer point;

    private LocalDateTime createdAt;

    @Builder
    public TraitAnswer(Integer id, TraitQuestion question, String content, String traitCode, int point, LocalDateTime createdAt) {
        this.id = id;
        this.question = question;
        this.content = content;
        this.traitCode = traitCode;
        this.point = point;
        this.createdAt = createdAt;
    }

    public void update(String newContent, String newTraitCode, int newPoint) {
        this.content = newContent;
        this.traitCode = newTraitCode;
        this.point = newPoint;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
