package com.eureka.mindbloom.commoncode.repository;

import com.eureka.mindbloom.commoncode.domain.CommonCodeGroup;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommonCodeGroupRepository extends JpaRepository<CommonCodeGroup, String> {

    @EntityGraph(attributePaths = {"parent"})
    List<CommonCodeGroup> findAll();
}