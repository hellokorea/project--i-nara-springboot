package com.eureka.mindbloom.trait.domain;

import com.eureka.mindbloom.common.domain.SoftDeleteEntity;
import com.eureka.mindbloom.member.domain.Child;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChildTrait extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

    private String traitGroupCode;

    private String traitValue;

    @Builder
    public ChildTrait(Long id, Child child, String traitGroupCode, String traitValue) {
        this.id = id;
        this.child = child;
        this.traitGroupCode = traitGroupCode;
        this.traitValue = traitValue;
    }

    public void update(String traitValue) {
        this.traitValue = traitValue;
    }
}
