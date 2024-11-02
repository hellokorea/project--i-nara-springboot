package com.eureka.mindbloom.event.domain;

import com.eureka.mindbloom.common.domain.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Event extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Integer winnersCount;

    public Event(LocalDateTime startTime, LocalDateTime endTime, Integer winnersCount) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.winnersCount = winnersCount;
    }
}