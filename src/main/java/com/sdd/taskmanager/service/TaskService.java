package com.sdd.taskmanager.service;

import com.sdd.taskmanager.model.Task;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaskService {
    /**
     * Creates a new task.
     * @param task The task object to create.
     * @return The created task.
     */
    Task createTask(Task task);
    /**
     * Finds a task by its ID.
     * @param id The ID of the task to find.
     * @return An Optional containing the task if found, otherwise empty.
     */
    Optional<Task> findTaskById(UUID id);
    /**
     * Retrieves all tasks.
     * @return A list of all tasks.
     */
    List<Task> findAllTasks();
    /**
     * Marks a task as complete.
     * @param id The ID of the task to complete.
     * @return The completed task.
     * @throws com.sdd.taskmanager.exception.TaskNotFoundException if the task is not found.
     */
    Task completeTask(UUID id);
}