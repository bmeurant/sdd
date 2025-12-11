package com.sdd.taskmanager.repository;

import com.sdd.taskmanager.model.Task;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface TaskRepository {
    Task create(Task task);
    Optional<Task> findById(UUID id);
    List<Task> findAll();
    void update(Task task);
}
