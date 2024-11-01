package com.eureka.mindbloom.category.repository;

import com.eureka.mindbloom.category.domain.CategoryTrait;
import com.eureka.mindbloom.category.domain.CategoryTraitId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryTraitRepository extends JpaRepository<CategoryTrait, CategoryTraitId> {

    // CategoryCode를 기준으로 다수의 CategoryTrait 검색
    @Query("SELECT ct FROM CategoryTrait ct WHERE ct.id.categoryCode = :categoryCode")
    List<CategoryTrait> findByIdCategoryCode(@Param("categoryCode") String categoryCode); // List로 반환
}
