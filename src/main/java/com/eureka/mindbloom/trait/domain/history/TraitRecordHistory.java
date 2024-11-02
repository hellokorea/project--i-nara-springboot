package com.eureka.mindbloom.trait.domain.history;

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
public class TraitRecordHistory extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Child child;

    private String traitCode;

    private String actionCode;

    private Integer point;

    private String bookName;

    @Builder
    public TraitRecordHistory(Long id, Child child, String traitCode, String actionCode, Integer point, String bookName) {
        this.id = id;
        this.child = child;
        this.traitCode = traitCode;
        this.actionCode = actionCode;
        this.point = point;
        this.bookName = bookName;
    }
}
