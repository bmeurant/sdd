package com.sdd.taskmanager.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sdd.taskmanager.dto.CreateTaskRequest;
import com.sdd.taskmanager.model.Task;
import com.sdd.taskmanager.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.ZonedDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTaskAndReturnCreatedStatus() throws Exception {
        // Given
        CreateTaskRequest request = new CreateTaskRequest("Controller Test Task", "Description for controller test task");

        UUID generatedId = UUID.randomUUID();
        ZonedDateTime createdAt = ZonedDateTime.now();
        Task createdTask = new Task(generatedId, request.getTitle(), request.getDescription(), false, createdAt);

        when(taskService.createTask(any(Task.class))).thenReturn(createdTask);

        // When & Then
        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(generatedId.toString()))
                .andExpect(jsonPath("$.title").value(request.getTitle()))
                .andExpect(jsonPath("$.description").value(request.getDescription()))
                .andExpect(jsonPath("$.completed").value(false));
    }

    @Test
    void shouldReturnBadRequestWhenTitleIsMissing() throws Exception {
        // Given
        CreateTaskRequest request = new CreateTaskRequest(null, "Description without title");

        // When & Then
        mockMvc.perform(post("/api/v1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").doesNotExist());
    }
}
