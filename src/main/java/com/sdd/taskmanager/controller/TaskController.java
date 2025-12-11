package com.sdd.taskmanager.controller;

import com.sdd.taskmanager.dto.CreateTaskRequest;
import com.sdd.taskmanager.exception.TaskNotFoundException;
import com.sdd.taskmanager.model.Task;
import com.sdd.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    /**
     * Constructs a new TaskController with the given TaskService.
     * @param taskService The service responsible for task operations.
     */
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    /**
     * Creates a new task.
     * @param request The request body containing the title and optional description of the task.
     * @return A ResponseEntity containing the created Task and HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<Task> createTask(@Valid @RequestBody CreateTaskRequest request) {
        Task newTask = new Task(request.getTitle(), request.getDescription());
        Task createdTask = taskService.createTask(newTask);
        return new ResponseEntity<>(createdTask, HttpStatus.CREATED);
    }

    /**
     * Retrieves a list of all tasks.
     * @return A ResponseEntity containing a list of Tasks and HTTP status 200 (OK).
     */
    @GetMapping
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.findAllTasks();
        return ResponseEntity.ok(tasks);
    }

    /**
     * Marks a task as completed.
     * @param id The UUID of the task to complete.
     * @return A ResponseEntity containing the updated Task and HTTP status 200 (OK).
     */
    @PatchMapping("/{id}/complete")
    public ResponseEntity<Task> completeTask(@PathVariable UUID id) {
        Task completedTask = taskService.completeTask(id);
        return ResponseEntity.ok(completedTask);
    }

    /**
     * Handles TaskNotFoundException and returns a 404 Not Found response.
     * @param ex The TaskNotFoundException that was thrown.
     * @return A ResponseEntity containing the exception message and HTTP status 404 (Not Found).
     */
    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleTaskNotFoundException(TaskNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}