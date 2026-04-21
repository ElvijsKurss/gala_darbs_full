package com.example.events.controller;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.events.dto.EventRequest;
import com.example.events.dto.EventResponse;
import com.example.events.service.EventService;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = "http://localhost:4200")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    public EventResponse createEvent(@RequestBody EventRequest request, @RequestParam String username) {
        return eventService.createEvent(request, username);
    }

    @GetMapping
    public List<EventResponse> getAllEvents(@RequestParam(required = false) String username) {
        return eventService.getAllEvents(username);
    }

    @PostMapping("/{eventId}/join")
    public String joinEvent(@PathVariable Long eventId, @RequestParam String username) {
        eventService.joinEvent(eventId, username);
        return "Pieteikšanās veiksmīga";
    }

    @DeleteMapping("/{eventId}/leave")
    public String leaveEvent(@PathVariable Long eventId, @RequestParam String username) {
        eventService.leaveEvent(eventId, username);
        return "Dalība atcelta";
    }

    @PutMapping("/{eventId}/cancel")
    public String cancelEvent(@PathVariable Long eventId, @RequestParam String username) {
        eventService.cancelEvent(eventId, username);
        return "Pasākums atcelts";
    }
}