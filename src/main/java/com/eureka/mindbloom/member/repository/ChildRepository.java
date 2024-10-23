package com.eureka.mindbloom.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eureka.mindbloom.member.domain.Child;

import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {
    Optional<Child> findChildById(Long childId);
}
