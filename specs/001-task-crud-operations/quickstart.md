# Quickstart: Task CRUD Operations

This guide provides instructions on how to build, run, and interact with the Task Manager API.

## Prerequisites

- Java 21
- Maven 3.8+

## Building the Application

1.  **Clone the repository** and navigate to the project root.
2.  **Switch to the feature branch**:
    ```sh
    git checkout 001-task-crud-operations
    ```
3.  **Build the project** using Maven:
    ```sh
    mvn clean install
    ```
    This will compile the code, run tests, and create an executable JAR file in the `target/` directory.

## Running the Application

Once the project is built, you can run the application using the following command:

```sh
java -jar target/taskmanager-0.0.1-SNAPSHOT.jar
```

The API will start on `http://localhost:8080`.

## Using the API

You can interact with the API using a tool like `curl` or Postman.

### 1. Create a Task

Send a `POST` request to `/api/v1/tasks` with a title and an optional description.

```sh
curl -X POST http://localhost:8080/api/v1/tasks \
-H "Content-Type: application/json" \
-d '{
  "title": "My first task",
  "description": "This is a detailed description of my first task."
}'
```

The server will respond with the newly created task object.

### 2. List All Tasks

Send a `GET` request to `/api/v1/tasks` to retrieve a list of all tasks.

```sh
curl -X GET http://localhost:8080/api/v1/tasks
```

The server will respond with a JSON array of task objects.

### 3. Complete a Task

To mark a task as completed, send a `PATCH` request to `/api/v1/tasks/{id}/complete`, replacing `{id}` with the actual ID of the task you want to complete.

First, get the ID from the list of tasks. Let's assume the ID is `123e4567-e89b-12d3-a456-426614174000`.

```sh
curl -X PATCH http://localhost:8080/api/v1/tasks/123e4567-e89b-12d3-a456-426614174000/complete
```

The server will respond with the updated task object, showing `completed: true`.

```