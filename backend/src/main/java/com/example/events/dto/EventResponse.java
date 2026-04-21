package com.example.events.dto;

public class EventResponse {

    private Long id;
    private String title;
    private String description;
    private String date;
    private String time;
    private String location;
    private Integer maxParticipants;
    private boolean cancelled;
    private long registeredCount;
    private boolean full;
    private boolean joined;
    private String createdByUsername;

    public EventResponse() {
    }

    public EventResponse(Long id, String title, String description, String date, String time,
            String location, Integer maxParticipants, boolean cancelled,
            long registeredCount, boolean full, boolean joined,
            String createdByUsername) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date = date;
        this.time = time;
        this.location = location;
        this.maxParticipants = maxParticipants;
        this.cancelled = cancelled;
        this.registeredCount = registeredCount;
        this.full = full;
        this.joined = joined;
        this.createdByUsername = createdByUsername;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public Integer getMaxParticipants() {
        return maxParticipants;
    }

    public boolean isCancelled() {
        return cancelled;
    }

    public long getRegisteredCount() {
        return registeredCount;
    }

    public boolean isFull() {
        return full;
    }

    public boolean isJoined() {
        return joined;
    }

    public String getCreatedByUsername() {
        return createdByUsername;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMaxParticipants(Integer maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public void setRegisteredCount(long registeredCount) {
        this.registeredCount = registeredCount;
    }

    public void setFull(boolean full) {
        this.full = full;
    }

    public void setJoined(boolean joined) {
        this.joined = joined;
    }

    public void setCreatedByUsername(String createdByUsername) {
        this.createdByUsername = createdByUsername;
    }
}