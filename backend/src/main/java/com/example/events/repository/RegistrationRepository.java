package com.example.events.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.events.entity.Event;
import com.example.events.entity.Registration;
import com.example.events.entity.User;

public interface RegistrationRepository extends JpaRepository<Registration, Long> {
    long countByEvent(Event event);

    boolean existsByUserAndEvent(User user, Event event);

    Optional<Registration> findByUserAndEvent(User user, Event event);
}