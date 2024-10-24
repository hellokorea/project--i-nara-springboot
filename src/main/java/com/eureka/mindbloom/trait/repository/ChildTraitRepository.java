package com.eureka.mindbloom.trait.repository;

import com.eureka.mindbloom.trait.domain.ChildTrait;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ChildTraitRepository extends JpaRepository<ChildTrait, Long> {

    @Query(value = """
           SELECT ct
           FROM ChildTrait  ct
           WHERE ct.child.id = :childId
        """)
    Optional<ChildTrait> findByChildId(@Param("childId") Long childId);
}
