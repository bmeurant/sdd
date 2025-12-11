package com.sdd.taskmanager.model;

import java.time.ZonedDateTime;
import java.util.UUID;

public class Task {
    private UUID id;
    private String title;
    private String description;
    private boolean completed;
    private ZonedDateTime createdAt;

    // Constructor for creating a new task (ID and createdAt are auto-generated)
    public Task(String title, String description) {
        this.title = title;
        this.description = description;
        this.completed = false; // Default to false
        this.createdAt = ZonedDateTime.now(); // Set current time, will be overwritten by DB
    }

    // Constructor for retrieving a task from the database
    public Task(UUID id, String title, String description, boolean completed, ZonedDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.completed = completed;
        this.createdAt = createdAt;
    }

    // Default constructor
    public Task() {
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Task{" +
               "id=" + id +
               ", title='" + title + "'" +
               ", description='" + description + "'" +
               ", completed=" + completed +
               ", createdAt=" + createdAt +
               "}";
    }
}
