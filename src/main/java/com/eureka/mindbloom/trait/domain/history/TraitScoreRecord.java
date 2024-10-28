package com.eureka.mindbloom.trait.domain.history;

import com.eureka.mindbloom.common.domain.SoftDeleteEntity;
import com.eureka.mindbloom.member.domain.Child;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TraitScoreRecord extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

    private String traitCode;

    private Integer traitScore;

    @Builder
    public TraitScoreRecord(Child child, String traitCode, Integer traitScore) {
        this.child = child;
        this.traitCode = traitCode;
        this.traitScore = traitScore;
    }

    public void updateScore(Integer newScore) {
        this.traitScore = newScore;
    }
}
