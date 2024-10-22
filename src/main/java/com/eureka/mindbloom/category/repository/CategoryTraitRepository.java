package com.eureka.mindbloom.category.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.eureka.mindbloom.category.domain.CategoryTrait;
import com.eureka.mindbloom.category.domain.CategoryTraitId;

public interface CategoryTraitRepository extends JpaRepository<CategoryTrait, CategoryTraitId> {
}
