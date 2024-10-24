package com.eureka.mindbloom.trait.domain;

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
public class ChildTrait extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

    private String traitGroupCode;

    private String traitValue;

    private boolean deleted;

    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    public ChildTrait(Long id, Child child, String traitGroupCode, String traitValue, boolean deleted, LocalDateTime createdAt, LocalDateTime deletedAt) {
        this.id = id;
        this.child = child;
        this.traitGroupCode = traitGroupCode;
        this.traitValue = traitValue;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.deletedAt = deletedAt;
    }

    public void update(String traitValue) {
        this.traitValue = traitValue;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
