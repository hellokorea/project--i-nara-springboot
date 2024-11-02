package com.eureka.mindbloom.member.domain;


import com.eureka.mindbloom.event.domain.EventParticipant;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member", indexes = {
        @Index(name = "idx_member_email", columnList = "email")
})
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String password;

    private String phone;

    private String role;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<Child> children = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<EventParticipant> eventRespons = new ArrayList<>();

    @Builder
    public Member(String name, String email, String password, String phone, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    public void addChild(Child child) {
        this.children.add(child);
    }

    public void updateMember(String name, String password, String phone) {
        this.name = name;
        this.password = password;
        this.phone = phone;
    }
}
