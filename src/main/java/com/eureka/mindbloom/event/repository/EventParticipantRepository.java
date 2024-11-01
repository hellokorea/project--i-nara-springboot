package com.eureka.mindbloom.event.repository;

import com.eureka.mindbloom.event.domain.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
}
