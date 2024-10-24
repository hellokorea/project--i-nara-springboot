package com.eureka.mindbloom.book.repository;

import com.eureka.mindbloom.common.domain.code.CommonCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {
    Optional<CommonCode> findByName(String name);
    Optional<CommonCode> findByCode(String code);
}