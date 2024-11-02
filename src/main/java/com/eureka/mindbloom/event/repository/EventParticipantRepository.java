package com.eureka.mindbloom.event.repository;

import com.eureka.mindbloom.event.domain.EventParticipant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {

    @Query("""
            SELECT p FROM EventParticipant p
            JOIN FETCH p.member m
            WHERE p.event.id = :eventId
            ORDER BY CAST(p.eventEntryTime AS double) ASC
            """)
    List<EventParticipant> findByEventIdOrderByValue(@Param("eventId") Integer eventId, Pageable pageable);
}
