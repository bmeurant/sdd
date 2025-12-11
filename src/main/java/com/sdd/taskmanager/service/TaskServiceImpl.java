package com.sdd.taskmanager.service;

import com.sdd.taskmanager.exception.TaskNotFoundException;
import com.sdd.taskmanager.model.Task;
import com.sdd.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    /**
     * Constructs a new TaskServiceImpl with the given TaskRepository.
     * @param taskRepository The repository responsible for task data access.
     */
    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task createTask(Task task) {
        return taskRepository.create(task);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Task> findTaskById(UUID id) {
        return taskRepository.findById(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> findAllTasks() {
        return taskRepository.findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task completeTask(UUID id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException("Task with id " + id + " not found"));
        task.setCompleted(true);
        taskRepository.update(task);
        return task;
    }
}