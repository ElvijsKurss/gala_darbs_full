package com.example.events.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.events.dto.EventRequest;
import com.example.events.dto.EventResponse;
import com.example.events.entity.Event;
import com.example.events.entity.Registration;
import com.example.events.entity.User;
import com.example.events.repository.EventRepository;
import com.example.events.repository.RegistrationRepository;
import com.example.events.repository.UserRepository;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final RegistrationRepository registrationRepository;
    private final UserRepository userRepository;

    public EventService(EventRepository eventRepository,
            RegistrationRepository registrationRepository,
            UserRepository userRepository) {
        this.eventRepository = eventRepository;
        this.registrationRepository = registrationRepository;
        this.userRepository = userRepository;
    }

    public EventResponse createEvent(EventRequest request, String username) {
        if (request.getTitle() == null || request.getTitle().isBlank()) {
            throw new RuntimeException("Nosaukums ir obligāts");
        }

        if (request.getDescription() == null || request.getDescription().isBlank()) {
            throw new RuntimeException("Apraksts ir obligāts");
        }

        if (request.getDate() == null || request.getDate().isBlank()) {
            throw new RuntimeException("Datums ir obligāts");
        }

        if (request.getTime() == null || request.getTime().isBlank()) {
            throw new RuntimeException("Laiks ir obligāts");
        }

        if (request.getLocation() == null || request.getLocation().isBlank()) {
            throw new RuntimeException("Vieta ir obligāta");
        }

        if (request.getMaxParticipants() == null || request.getMaxParticipants() < 1) {
            throw new RuntimeException("Maksimālajam dalībnieku skaitam jābūt vismaz 1");
        }

        LocalDate eventDate = LocalDate.parse(request.getDate());
        LocalTime eventTime = LocalTime.parse(request.getTime());

        if (eventDate.isBefore(LocalDate.now())) {
            throw new RuntimeException("Pasākumu nedrīkst izveidot pagātnē");
        }

        User creator = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Lietotājs nav atrasts"));

        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setDate(eventDate);
        event.setTime(eventTime);
        event.setLocation(request.getLocation());
        event.setMaxParticipants(request.getMaxParticipants());
        event.setCancelled(false);
        event.setCreatedBy(creator);

        Event saved = eventRepository.save(event);

        return mapToResponse(saved, creator);
    }

    public List<EventResponse> getAllEvents(String username) {
        User user = null;

        if (username != null && !username.isBlank()) {
            user = userRepository.findByUsername(username).orElse(null);
        }

        User finalUser = user;

        return eventRepository.findAll()
                .stream()
                .map(event -> mapToResponse(event, finalUser))
                .toList();
    }

    public void joinEvent(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Pasākums nav atrasts"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Lietotājs nav atrasts"));

        if (event.isCancelled()) {
            throw new RuntimeException("Pasākums ir atcelts");
        }

        if (registrationRepository.existsByUserAndEvent(user, event)) {
            throw new RuntimeException("Tu jau esi pieteicies uz šo pasākumu");
        }

        long registeredCount = registrationRepository.countByEvent(event);
        if (registeredCount >= event.getMaxParticipants()) {
            throw new RuntimeException("Pasākums ir pilns");
        }

        Registration registration = new Registration(user, event);
        registrationRepository.save(registration);
    }

    public void leaveEvent(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Pasākums nav atrasts"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Lietotājs nav atrasts"));

        Registration registration = registrationRepository.findByUserAndEvent(user, event)
                .orElseThrow(() -> new RuntimeException("Tu neesi pieteicies uz šo pasākumu"));

        registrationRepository.delete(registration);
    }

    public void cancelEvent(Long eventId, String username) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Pasākums nav atrasts"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Lietotājs nav atrasts"));

        if (event.getCreatedBy() == null || !event.getCreatedBy().getId().equals(user.getId())) {
            throw new RuntimeException("Tikai pasākuma izveidotājs drīkst to atcelt");
        }

        event.setCancelled(true);
        eventRepository.save(event);
    }

    private EventResponse mapToResponse(Event event, User user) {
        long registeredCount = registrationRepository.countByEvent(event);

        int maxParticipants = event.getMaxParticipants() != null ? event.getMaxParticipants() : 0;
        boolean full = registeredCount >= maxParticipants;

        boolean joined = false;
        if (user != null) {
            joined = registrationRepository.existsByUserAndEvent(user, event);
        }

        String createdByUsername = null;
        if (event.getCreatedBy() != null) {
            createdByUsername = event.getCreatedBy().getUsername();
        }

        return new EventResponse(
                event.getId(),
                event.getTitle() != null ? event.getTitle() : "",
                event.getDescription() != null ? event.getDescription() : "",
                event.getDate() != null ? event.getDate().toString() : "",
                event.getTime() != null ? event.getTime().toString() : "",
                event.getLocation() != null ? event.getLocation() : "",
                maxParticipants,
                event.isCancelled(),
                registeredCount,
                full,
                joined,
                createdByUsername);
    }
}