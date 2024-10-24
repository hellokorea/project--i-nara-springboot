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
public class TraitQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String content;

    private String traitCodeGroup;

    private boolean disabled;

    private LocalDateTime createdAt;

    @Builder
    public TraitQuestion(Integer id, String content, String traitCodeGroup, boolean disabled, LocalDateTime createdAt) {
        this.id = id;
        this.content = content;
        this.traitCodeGroup = traitCodeGroup;
        this.disabled = disabled;
        this.createdAt = createdAt;
    }

    public void update(String newContent, boolean newDisabled ) {
        this.content = newContent;
        this.disabled = newDisabled;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}

