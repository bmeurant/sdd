package com.sdd.taskmanager.repository;

import com.sdd.taskmanager.model.Task;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class JdbcTaskRepository implements TaskRepository {

    private final JdbcTemplate jdbcTemplate;

    /**
     * Constructs a new JdbcTaskRepository with the given JdbcTemplate.
     * @param jdbcTemplate The JdbcTemplate to use for database operations.
     */
    public JdbcTaskRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task create(Task task) {
        UUID id = UUID.randomUUID();
        ZonedDateTime createdAt = ZonedDateTime.now(ZoneOffset.UTC); // Use UTC for consistency

        String sql = "INSERT INTO tasks (id, title, description, completed, created_at) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
                id.toString(),
                task.getTitle(),
                task.getDescription(),
                false, // Always false on creation as per data-model.md
                createdAt.toOffsetDateTime() // Store ZonedDateTime as OffsetDateTime in DB
        );

        // Return the task with the generated ID and createdAt timestamp
        Task createdTask = new Task(id, task.getTitle(), task.getDescription(), false, createdAt);
        return createdTask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Task> findById(UUID id) {
        String sql = "SELECT id, title, description, completed, created_at FROM tasks WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(sql, this::mapRowToTask, id.toString()));
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> findAll() {
        String sql = "SELECT id, title, description, completed, created_at FROM tasks";
        return jdbcTemplate.query(sql, this::mapRowToTask);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Task task) {
        String sql = "UPDATE tasks SET title = ?, description = ?, completed = ? WHERE id = ?";
        jdbcTemplate.update(sql,
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getId().toString()
        );
    }

    /**
     * Maps a row from the ResultSet to a Task object.
     * @param rs The ResultSet to map.
     * @param rowNum The number of the current row.
     * @return A Task object mapped from the current row.
     * @throws SQLException if a SQL error occurs.
     */
    private Task mapRowToTask(ResultSet rs, int rowNum) throws SQLException {
        return new Task(
                UUID.fromString(rs.getString("id")),
                rs.getString("title"),
                rs.getString("description"),
                rs.getBoolean("completed"),
                rs.getObject("created_at", ZonedDateTime.class)
        );
    }
}