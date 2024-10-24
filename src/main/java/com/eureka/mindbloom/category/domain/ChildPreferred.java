package com.eureka.mindbloom.category.domain;

import com.eureka.mindbloom.member.domain.Child;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChildPreferred {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "CHAR(4)")
    private String categoryCode;

    @ManyToOne
    @JoinColumn(name = "child_id")
    private Child child;

    public ChildPreferred(String categoryCode, Child child) {
        this.categoryCode = categoryCode;
        this.child = child;
    }
}
