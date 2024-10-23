package com.eureka.mindbloom.member.repository;

import com.eureka.mindbloom.member.domain.Child;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByParentId(Long id);
}
