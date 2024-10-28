package com.eureka.mindbloom.event.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventQuestionLink {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "SMALLINT")
    private Integer sequence;

    @ManyToOne(fetch = FetchType.LAZY)
    private EventQuestion eventQuestion;

    @OneToMany(mappedBy = "eventQuestionLink")
    private List<EventQuestionContentLink> eventQuestionContentLinks = new ArrayList<>();
}