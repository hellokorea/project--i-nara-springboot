package com.eureka.mindbloom.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eureka.mindbloom.member.domain.Child;

public interface ChildRepository extends JpaRepository<Child, Long> {
}
