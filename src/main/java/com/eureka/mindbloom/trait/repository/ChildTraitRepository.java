package com.eureka.mindbloom.trait.repository;

import com.eureka.mindbloom.trait.domain.ChildTrait;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChildTraitRepository extends JpaRepository<ChildTrait, Long> {

    @Query(value = """
               SELECT ct
               FROM ChildTrait ct
               WHERE ct.child.id = :childId
               order by ct.createdAt desc
               limit 1
            """)
    Optional<ChildTrait> findByChildId(@Param("childId") Long childId);

    @Query(value = """
           SELECT ct.traitValue
           FROM ChildTrait ct
           WHERE ct.child.id = :childId
           AND ct.deletedAt IS NULL
           ORDER BY ct.createdAt DESC
           """)
    List<String> findChildCurrentTraitByTraitValue(@Param("childId") Long childId, Pageable pageable);

    @Query(value = """
           SELECT ct
           FROM ChildTrait ct
           WHERE ct.child.id = :childId
           AND ct.deletedAt IS NULL
           ORDER BY ct.createdAt DESC
           """)
    List<ChildTrait> findChildTraitByDeletedAtIsNull(@Param("childId") Long childId, Pageable pageable);

    @Query("""
           SELECT ct
           FROM ChildTrait ct
           WHERE ct.child.id = :childId
           AND ct.deletedAt IS NULL
           ORDER BY ct.createdAt DESC
           """)
    List<ChildTrait> findChildTraitByDeletedAtIsNull(@Param("childId") Long childId);

    @Modifying
    @Query(nativeQuery = true, value = """
               UPDATE child_trait ct
                JOIN trait_score_record tsr ON ct.child_id = tsr.child_id
                JOIN trait_score_daily_record tsd ON tsr.id = tsd.trait_score_record_id
                JOIN trait_record_history rh ON ct.child_id = rh.child_id
               SET ct.deleted_at = now(), tsr.deleted_at = now(), tsd.deleted_at = now(), rh.deleted_at = now()
               WHERE ct.child_id = :childId AND ct.deleted_at IS NULL
            """)
    void softDeleteChildTrait(Long childId);
}
