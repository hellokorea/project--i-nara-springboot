package com.eureka.mindbloom.trait.domain.history;

import com.eureka.mindbloom.common.domain.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TraitScoreDailyRecord extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TraitScoreRecord traitScoreRecordId;

    private String traitCode;

    private String traitScore;

    @Builder
    public TraitScoreDailyRecord(TraitScoreRecord traitScoreRecordId, String traitCode, String traitScore) {
        this.traitScoreRecordId = traitScoreRecordId;
        this.traitCode = traitCode;
        this.traitScore = traitScore;
    }
}
