package com.eureka.mindbloom.member.repository;

import com.eureka.mindbloom.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    boolean existsByEmail(String email);

    @Query("""
            select m from Member m
            left join fetch m.children c
            where m.email = :email
            """)
    Optional<Member> findByEmail(String email);
}
