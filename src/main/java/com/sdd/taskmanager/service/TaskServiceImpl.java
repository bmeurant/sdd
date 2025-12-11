package com.sdd.taskmanager.service;

import com.sdd.taskmanager.model.Task;
import com.sdd.taskmanager.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(Task task) {
        return taskRepository.create(task);
    }

    @Override
    public Optional<Task> findTaskById(UUID id) {
        // This will be implemented later
        return Optional.empty();
    }

    @Override
    public List<Task> findAllTasks() {
        // This will be implemented later
        return List.of();
    }

    @Override
    public Task completeTask(UUID id) {
        // This will be implemented later
        return null;
    }
}
