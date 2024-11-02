package com.eureka.mindbloom.member.domain;

import com.eureka.mindbloom.category.domain.ChildPreferred;
import com.eureka.mindbloom.common.domain.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "child", indexes = {
        @Index(name = "idx_child_parent_id", columnList = "parent_id")
})
public class Child extends SoftDeleteEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String gender;

    private LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Member parent;

    @OneToMany(mappedBy = "child", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<ChildPreferred> preferredContents = new ArrayList<>();

    @Builder
    public Child(String name, String gender, LocalDate birthDate) {
        this.name = name;
        this.gender = gender;
        this.birthDate = birthDate;
    }

    public void updateParent(Member parent) {
        this.parent = parent;
        parent.addChild(this);
    }

    public void addPreferredContent(List<ChildPreferred> preferredContents) {
        this.preferredContents.addAll(preferredContents);
    }

    public void updateChild(String name, List<ChildPreferred> preferredContents) {
        this.name = name;
        this.preferredContents.clear();
        addPreferredContent(preferredContents);
    }
}
