package com.eureka.mindbloom.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eureka.mindbloom.member.domain.Member;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
