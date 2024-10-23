package com.eureka.mindbloom.member.repository;

import com.eureka.mindbloom.member.domain.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByParentId(Long id);

    @Query("""
            select c from Child c
            left join fetch c.preferredContents
            where c.id = :id and c.parent.id = :parentId
            """
    )
    Optional<Child> findByParentIdAndId(Long parentId, Long id);
}
