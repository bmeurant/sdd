package com.sdd.taskmanager.service;

import com.sdd.taskmanager.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    Task createTask(Task task);
    Optional<Task> findTaskById(UUID id);
    List<Task> findAllTasks();
    Task completeTask(UUID id);
}
