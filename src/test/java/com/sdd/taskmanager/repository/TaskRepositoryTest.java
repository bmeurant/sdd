package com.sdd.taskmanager.repository;

import com.sdd.taskmanager.model.Task;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@JdbcTest
@Import(JdbcTaskRepository.class) // Import the repository implementation
@Sql(scripts = {"classpath:schema.sql"}) // Load the schema for testing
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository; // Autowire the interface

    @Test
    void contextLoads() {
        assertThat(taskRepository).isNotNull();
    }

    @Test
    void shouldCreateTaskAndReturnIt() {
        // Given
        String title = "Test Task";
        String description = "Description for test task";
        Task newTask = new Task(title, description);

        // When
        Task createdTask = taskRepository.create(newTask);

        // Then
        assertNotNull(createdTask);
        assertNotNull(createdTask.getId());
        assertThat(createdTask.getTitle()).isEqualTo(title);
        assertThat(createdTask.getDescription()).isEqualTo(description);
        assertThat(createdTask.isCompleted()).isFalse();
        assertNotNull(createdTask.getCreatedAt());

        // Verify it can be found in the database
        Optional<Task> foundTask = taskRepository.findById(createdTask.getId());
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getTitle()).isEqualTo(title);
        assertThat(foundTask.get().getDescription()).isEqualTo(description);
        assertThat(foundTask.get().isCompleted()).isFalse();
        assertThat(foundTask.get().getCreatedAt()).isEqualToIgnoringNanos(createdTask.getCreatedAt()); // Compare ignoring nanos due to potential DB precision differences
    }
}
