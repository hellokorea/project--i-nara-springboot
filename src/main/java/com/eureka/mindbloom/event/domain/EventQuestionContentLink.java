package com.eureka.mindbloom.event.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventQuestionContentLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private boolean required;

    private boolean isText;

    @Column(columnDefinition = "SMALLINT")
    private Integer sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventQuestionContent eventQuestionContent;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventQuestionLink eventQuestionLink;
}