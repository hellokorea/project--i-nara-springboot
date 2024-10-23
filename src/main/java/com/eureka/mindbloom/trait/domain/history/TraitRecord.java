package com.eureka.mindbloom.trait.domain.history;

import com.eureka.mindbloom.common.domain.SoftDeleteEntity;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.trait.domain.ChildTrait;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TraitRecord extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_trait_id")
    private ChildTrait childTrait;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

    private String traitCode;

    private Integer traitScore;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public TraitRecord(Long id, ChildTrait childTrait, Child child, String traitCode, Integer traitScore, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.id = id;
        this.childTrait = childTrait;
        this.child = child;
        this.traitCode = traitCode;
        this.traitScore = traitScore;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
