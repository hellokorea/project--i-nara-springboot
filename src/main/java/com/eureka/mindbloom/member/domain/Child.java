package com.eureka.mindbloom.member.domain;

import com.eureka.mindbloom.common.domain.SoftDeleteEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Child extends SoftDeleteEntity {

    @Id
    private Long id;

    private String name;

    private String gender;

    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member parent;
}
