package com.eureka.mindbloom.event.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private LocalDateTime startedAt;

    private LocalDateTime endedAt;

    private Integer winner;

    @ManyToOne(fetch = FetchType.LAZY)
    private Event event;
}