package com.sdd.taskmanager.repository;

import com.sdd.taskmanager.model.Task;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface TaskRepository {
    /**
     * Creates a new task in the repository.
     * @param task The task object to create.
     * @return The created task with its generated ID and creation timestamp.
     */
    Task create(Task task);
    /**
     * Finds a task by its unique ID.
     * @param id The UUID of the task to find.
     * @return An Optional containing the task if found, otherwise empty.
     */
    Optional<Task> findById(UUID id);
    /**
     * Retrieves a list of all tasks from the repository.
     * @return A list of all tasks.
     */
    List<Task> findAll();
    /**
     * Updates an existing task in the repository.
     * @param task The task object with updated information.
     */
    void update(Task task);
}