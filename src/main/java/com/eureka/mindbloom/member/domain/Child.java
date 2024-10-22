package com.eureka.mindbloom.member.domain;

import com.eureka.mindbloom.common.domain.SoftDeleteEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Child extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String gender;

    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member parent;

    @Builder
    public Child(String name, String gender, LocalDate birthDate, Member parent) {
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
        this.parent = parent;
    }
}
