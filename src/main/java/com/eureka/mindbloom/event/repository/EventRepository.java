package com.eureka.mindbloom.event.repository;

import com.eureka.mindbloom.event.domain.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
