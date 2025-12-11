# Tasks: Task CRUD Operations

**Input**: Design documents from `/specs/001-task-crud-operations/`
**Prerequisites**: plan.md, spec.md, data-model.md, contracts/

## Phase 1: Setup

**Purpose**: Project initialization and basic structure.

- [x] T001 Initialize a new Spring Boot project using Maven in the repository root.
- [x] T002 Add required dependencies to `pom.xml`: `spring-boot-starter-web`, `spring-boot-starter-jdbc`, `h2`, `spring-boot-starter-test`, `jakarta.validation:jakarta.validation-api`.
- [x] T003 Configure the H2 database and server port in `src/main/resources/application.properties`.

---

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core data structure and database schema.

- [ ] T004 Create the `Task` model class in `src/main/java/com/sdd/taskmanager/model/Task.java`.
- [ ] T005 Define the database schema for the `tasks` table in `src/main/resources/schema.sql`.

---

## Phase 3: User Story 1 - Create a Task (Priority: P1) 🎯 MVP

**Goal**: Allow users to create a new task.
**Independent Test**: A user can call the `POST /api/v1/tasks` endpoint and verify the new task is created and returned with a 201 status.

### Tests for User Story 1

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T006 [P] [US1] Write repository test for creating a task in `src/test/java/com/sdd/taskmanager/repository/TaskRepositoryTest.java`.
- [ ] T007 [P] [US1] Write service test for the `createTask` logic in `src/test/java/com/sdd/taskmanager/service/TaskServiceTest.java`.
- [ ] T008 [P] [US1] Write controller integration test for the `POST /api/v1/tasks` endpoint in `src/test/java/com/sdd/taskmanager/controller/TaskControllerTest.java`.

### Implementation for User Story 1

- [ ] T009 [P] [US1] Create the `CreateTaskRequest` DTO in `src/main/java/com/sdd/taskmanager/dto/CreateTaskRequest.java` with validation annotations.
- [ ] T010 [US1] Implement the `create` method in `src/main/java/com/sdd/taskmanager/repository/TaskRepository.java` using `JdbcTemplate`.
- [ ] T011 [US1] Implement the `createTask` method in `src/main/java/com/sdd/taskmanager/service/TaskService.java`.
- [ ] T012 [US1] Implement the `createTask` endpoint in `src/main/java/com/sdd/taskmanager/controller/TaskController.java`.

---

## Phase 4: User Story 2 - List All Tasks (Priority: P2)

**Goal**: Allow users to see all created tasks.
**Independent Test**: A user can call the `GET /api/v1/tasks` endpoint and receive a list of all previously created tasks.

### Tests for User Story 2

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T013 [P] [US2] Write repository test for finding all tasks.
- [ ] T014 [P] [US2] Write service test for the `findAllTasks` logic.
- [ ] T015 [P] [US2] Write controller integration test for the `GET /api/v1/tasks` endpoint.

### Implementation for User Story 2

- [ ] T016 [US2] Implement the `findAll` method in `src/main/java/com/sdd/taskmanager/repository/TaskRepository.java`.
- [ ] T017 [US2] Implement the `findAllTasks` method in `src/main/java/com/sdd/taskmanager/service/TaskService.java`.
- [ ] T018 [US2] Implement the `findAllTasks` endpoint in `src/main/java/com/sdd/taskmanager/controller/TaskController.java`.

---

## Phase 5: User Story 3 - Complete a Task (Priority: P3)

**Goal**: Allow users to mark a task as complete.
**Independent Test**: A user can call the `PATCH /api/v1/tasks/{id}/complete` endpoint and verify the task's `completed` status is updated to `true`.

### Tests for User Story 3

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [ ] T019 [P] [US3] Write repository test for finding a task by ID and updating its status.
- [ ] T020 [P] [US3] Write service test for the `completeTask` logic, including handling for non-existent tasks.
- [ ] T021 [P] [US3] Write controller integration test for the `PATCH /api/v1/tasks/{id}/complete` endpoint.

### Implementation for User Story 3

- [ ] T022 [US3] Implement the `findById` and `update` methods in `src/main/java/com/sdd/taskmanager/repository/TaskRepository.java`.
- [ ] T023 [US3] Implement the `completeTask` method in `src/main/java/com/sdd/taskmanager/service/TaskService.java`.
- [ ] T024 [US3] Implement the `completeTask` endpoint in `src/main/java/com/sdd/taskmanager/controller/TaskController.java`.

---

## Phase N: Polish & Cross-Cutting Concerns

- [ ] T025 [P] Add Javadoc to all new public methods in the `controller`, `service`, and `repository` packages.
- [ ] T026 [P] Configure detailed monitoring (logging, metrics) as per NFR-004.
- [ ] T027 Run `mvn test` and verify code coverage is >= 80%.
- [ ] T028 Validate all API endpoints using the `quickstart.md` guide.

---

## Dependencies & Execution Order

- **Phase 1 & 2**: Must be completed before any user story work begins.
- **User Stories (Phase 3-5)**: Can be implemented in priority order (US1 → US2 → US3). While some tasks within a story are parallelizable, the stories themselves are mostly sequential (listing and completing tasks depends on creating them first).
- **Final Phase (Polish)**: Can be done after all user stories are complete.
