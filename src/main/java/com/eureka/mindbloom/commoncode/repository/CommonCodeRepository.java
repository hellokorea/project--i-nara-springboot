package com.eureka.mindbloom.commoncode.repository;


import com.eureka.mindbloom.commoncode.domain.CommonCode;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {
    Optional<CommonCode> findByName(String name);

    Optional<CommonCode> findByCode(String code);

    @EntityGraph(attributePaths = {"codeGroup", "codeGroup.parent"})
    List<CommonCode> findAll();
}