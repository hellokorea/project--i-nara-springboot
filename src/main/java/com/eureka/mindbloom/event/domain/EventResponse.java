package com.eureka.mindbloom.event.domain;

import com.eureka.mindbloom.common.domain.BaseEntity;
import com.eureka.mindbloom.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EventResponse extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String response;

    @ManyToOne
    private Member member;

    @ManyToOne
    private EventQuestionContentLink eventQuestionContentLink;
}