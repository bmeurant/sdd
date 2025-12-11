package com.sdd.taskmanager.service;

import com.sdd.taskmanager.exception.TaskNotFoundException;
import com.sdd.taskmanager.model.Task;
import com.sdd.taskmanager.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void shouldCreateTaskSuccessfully() {
        // Given
        String title = "Service Test Task";
        String description = "Description for service test task";
        Task newTask = new Task(title, description);

        UUID generatedId = UUID.randomUUID();
        ZonedDateTime createdAt = ZonedDateTime.now();
        Task savedTask = new Task(generatedId, title, description, false, createdAt);

        when(taskRepository.create(any(Task.class))).thenReturn(savedTask);

        // When
        Task result = taskService.createTask(newTask);

        // Then
        assertNotNull(result);
        assertThat(result.getId()).isEqualTo(generatedId);
        assertThat(result.getTitle()).isEqualTo(title);
        assertThat(result.getDescription()).isEqualTo(description);
        assertThat(result.isCompleted()).isFalse();
        assertThat(result.getCreatedAt()).isEqualTo(createdAt);
    }

    @Test
    void shouldFindAllTasks() {
        // Given
        Task task1 = new Task(UUID.randomUUID(), "Task 1", "Description 1", false, ZonedDateTime.now());
        Task task2 = new Task(UUID.randomUUID(), "Task 2", "Description 2", false, ZonedDateTime.now());
        List<Task> mockTasks = Arrays.asList(task1, task2);

        when(taskRepository.findAll()).thenReturn(mockTasks);

        // When
        List<Task> result = taskService.findAllTasks();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(mockTasks.toArray(new Task[0]));
    }

    @Test
    void shouldCompleteTask() {
        // Given
        UUID taskId = UUID.randomUUID();
        Task existingTask = new Task(taskId, "Existing Task", "Description", false, ZonedDateTime.now());
        Optional<Task> optionalTask = Optional.of(existingTask);

        when(taskRepository.findById(taskId)).thenReturn(optionalTask);
        doAnswer(invocation -> {
            Task taskToUpdate = invocation.getArgument(0);
            assertThat(taskToUpdate.isCompleted()).isTrue(); // Verify task is marked completed
            return null; // void method returns null
        }).when(taskRepository).update(any(Task.class));

        // When
        Task completedTask = taskService.completeTask(taskId);

        // Then
        assertThat(completedTask).isNotNull();
        assertThat(completedTask.getId()).isEqualTo(taskId);
        assertThat(completedTask.isCompleted()).isTrue();
        verify(taskRepository, times(1)).findById(taskId);
        verify(taskRepository, times(1)).update(any(Task.class));
    }

    @Test
    void shouldThrowExceptionWhenCompletingNonExistentTask() {
        // Given
        UUID nonExistentId = UUID.randomUUID();
        when(taskRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(TaskNotFoundException.class, () -> taskService.completeTask(nonExistentId));
        verify(taskRepository, times(1)).findById(nonExistentId);
        verify(taskRepository, times(0)).update(any(Task.class)); // Ensure update is not called
    }
}
