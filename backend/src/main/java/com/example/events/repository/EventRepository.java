package com.example.events.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.events.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}