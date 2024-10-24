package com.eureka.mindbloom.category.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.eureka.mindbloom.category.domain.ChildPreferred;

public interface ChildPreferredRepository extends JpaRepository<ChildPreferred, Long> {
	@Query("SELECT distinct cc.code FROM ChildPreferred cp JOIN CommonCode cc ON cp.categoryCode = cc.codeGroup.codeGroup WHERE cp.child.id = :childId")
	List<String> findCategoryCodeByChildId(Long childId);
}
