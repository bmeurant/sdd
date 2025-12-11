# Spec-Driven Development (SDD) Tutorial Using SpecKit and Gemini

This tutorial guides you step-by-step through setting up and using **Spec-Driven Development (SDD)**. We will use **Specify CLI** (referred to as "speckit") to orchestrate the workflow and **Gemini CLI** for intelligent content and code generation.

## 1. Key Concepts of SDD

SDD structures AI-assisted development into 4 distinct phases, each producing a validated artifact before moving to the next:

1.  **Constitution** (`constitution.md`): The non-negotiable rules. Command: `/speckit.constitution`
2.  **Specify** (`spec.md`): Functional requirements. Command: `/speckit.specify`
3.  **Plan** (`plan.md`): Technical breakdown. Command: `/speckit.plan`
4.  **Task & Execute** (`tasks.md`): Actionable tasks. Command: `/speckit.tasks` then `/speckit.implement`

## 2. Prerequisites and Installation

### Necessary Tools
Ensure you have the following installed in your environment (Linux):

1.  **Python 3.8+** and `uv` (recommended for installing Specify CLI).
2.  **Gemini CLI**: To interact with the AI from the terminal.
3.  **VSCode** or **IntelliJ** with the **Gemini Code Assist** extension.
4.  **Java 21** and **Maven** (for this specific use case).

### Installing Specify CLI
The **Specify CLI** is managed by the [official Spec Kit repository](https://github.com/github/spec-kit) and is best installed using uv, the modern Python package installer.

```bash
# 1. Install 'uv' using pip (assuming pip is already available with Python)
# If pip is not available, you may need to install it first.
pip install uv

# 2. Use 'uv' to install the Specify CLI package
uv tool install specify-cli --from git+https://github.com/github/spec-kit.git

# 3. Verify installation
specify --help
```

## 3. Project Initialization

> [!CAUTION]
> **Git Root Requirement**: You **MUST** be at the root of your Git repository.
> Speckit relies heavily on Git context. Initializing in a subdirectory (e.g., `backend/`) causes LLM errors (wrong paths, loops).

```bash
# Navigate to your project root
cd /path/to/your/project

# Initialize the Spec Kit, binding Gemini as the AI agent. Press y if asked
specify init --here --ai gemini

# Optional check to confirm all tools are installed
specify check
```

**Result**: 

```bash
Checking for installed tools...

Check Available Tools
├── ● Git version control (available)
├── ○ GitHub Copilot (IDE-based, no CLI check)
├── ● Claude Code (available)
├── ● Gemini CLI (available)
├── ○ Cursor (IDE-based, no CLI check)
├── ● Qwen Code (not found)
├── ● opencode (not found)
├── ● Codex CLI (not found)
├── ○ Windsurf (IDE-based, no CLI check)
├── ○ Kilo Code (IDE-based, no CLI check)
├── ● Auggie CLI (not found)
├── ● CodeBuddy (not found)
├── ● Qoder CLI (not found)
├── ○ Roo Code (IDE-based, no CLI check)
├── ● Amazon Q Developer CLI (not found)
├── ● Amp (not found)
├── ● SHAI (not found)
├── ○ IBM Bob (IDE-based, no CLI check)
├── ● Visual Studio Code (available)
└── ● Visual Studio Code Insiders (not found)

Specify CLI is ready to use!
```

This creates the `.specify` and the `.gemini` directories. These directories abstract the complexity of context management and tool integration.

### Understanding the Generated Structure

*   **`.specify/` (The Process)**: The core orchestrator enforcing the **Gated Flow** (Constitution → Spec → Plan → Task).
    *   **`memory`**: Stores current state and validated artifacts (`spec.md`, `plan.md`) to ensure context.
    *   **`templates`**: Markdown blueprints for artifacts, ensuring standard documentation formats.
    *   **`scripts`**: Custom hooks to extend the CLI behavior.

*   **`.gemini/` (The Tool)**: The abstraction layer for the AI agent.
    *   **Configuration**: Stores model preferences (e.g., `gemini-2.5-pro`) and parameters.
    *   **System Prompts**: Specialized instructions passed to Gemini for each SDD phase (e.g., "Act as an Architect").

> **In short**: `.specify` manages the **HOW** (Process), while `.gemini` manages the **WHO** (AI Tool).

Once done, launch `gemini` in your project directory and start typing `speckit` you'll see the list of all speckit commands directly integrated into you gemini cli:

![Gemini speckit commands](./images/gemini-speckit-commands.png)

## 4. Concrete Use Case: Simple Task Manager API

We will apply the SDD workflow to develop a **Task Manager API** (Java/Spring Boot) that exposes CRUD endpoints for tasks and persists them in an in-memory database (H2). This ensures the app runs immediately without external middleware.

### Step 1: The Constitution (`.specify/constitution.md`)

This is the foundational step. Edit `.specify/constitution.md` to reflect the strict constraints for your entire project.

#### 1. Use the integrated specify constitution command, within Gemini CLI, to generate the file structure and provide the required context.

```markdown
# Use the integrated command to start the generation of the constitution.md file
/speckit.constitution Create principles for this project. Ask, don't guess
```

**Result**: Gemini should answer something like this, since we did not provide any argument:

*Note: Remember that this is a LLM so answers can be different. It could ask for name and description only and then principles or all at the same time.*

```markdown
✦ I'm ready to create the project constitution. First, what is the name of this project? I also need to know the core
principles you want to establish. Please provide at least one, and I will follow up with any necessary questions.
```
        
#### 2. In any case, provide this answer (using `CTRL-X`):

```markdown
# Project Constitution: Task Manager API
# Ratification Date: today

## Tech Stack
- **Language**: Java 21
- **Framework**: Spring Boot 3.5.8
- **Database**: H2 (In-Memory)
- **Build Tool**: Maven

## Architectural Constraints (Strict)
- **No ORMs**: Strictly forbidden (Hibernate, JPA, etc.). Use **JdbcTemplate** exclusively for database access.
- **Layered Architecture**: Controller -> Service -> Repository.
- **Validation**: Use `jakarta.validation` constraints on DTOs.

## Coding Standards
- **Testing**: JUnit 5 and Mockito. **Minimum 80% code coverage** enforced.
- **Documentation**: Javadoc on all public methods.
- **Style**: Google Java Style Guide.
- **Commits**: Follow **Conventional Commits** (e.g., `feat: add task endpoint`).
```

> [!TIP]
> **Where to put Technical Requirements?**
> *   **Constitution**: Global, non-negotiable rules that apply to **every** feature (e.g., "Always use JdbcTemplate", "Java 21").
> *   **Specify**: Constraints specific to **this** feature only (e.g., "This endpoint must respond in < 50ms", "Use a Redis set for this specific counter").


*Allow actions & Accept modifications from Gemini if asked*

**Results**: 

* After some readings (templates, configurations, etc.), a brand new `constitution.md` file should have been generated. See [constitution.md](./.specify/memory/constitution.md)

```markdown
✦ All templates have been successfully updated to align with the new constitution.

Here is a summary of the actions taken:
- Created Constitution: Drafted and wrote v1.0.0 of the project constitution to .specify/memory/constitution.md. This
    was a major version bump (from 0.0.0 to 1.0.0) as it establishes the initial set of governing principles.
- Updated Plan Template: Aligned .specify/templates/plan-template.md with the project's specific tech stack and added
    a "Constitution Check" section to enforce the new rules.
- Updated Spec Template: Added a "Constitutional Adherence" section to .specify/templates/spec-template.md to ensure
    feature design respects the project's core principles.
- Updated Tasks Template: Modified .specify/templates/tasks-template.md to use Java/Maven conventions and reflect the
    mandatory testing requirement.

All files are now in sync.
```

> [!NOTE]
> [plan-template.md](./.specify/templates/plan-template.md), [spec-template.md](./.specify/templates/spec-template.md) and [tasks-template.md](./.specify/templates/tasks-template.md) should also have been updated. This will ensure that your core principles will be effectively checked during specify, plan and implementation phases.

#### 3. At this stage you can **review, update and iterate** on the constitution file including all your requirements, context, etc.

> [!WARNING]
> **Syncing Changes**: If you edit `constitution.md` manually, you **must** run `/speckit.constitution` again.
> This ensures that the changes are propagated to the `.specify/templates/` (e.g., updating the Plan template to reflect new architectural rules) and the AI's system prompts.

### Step 2: Specify

We define the feature functionally.

#### 1. Create the spec

```bash
/speckit.specify --new "Task CRUD Operations"
```

**Result**: Gemini should answer something like this:

*Note: Remember that this is a LLM so answers can be different.*

```markdown
✦ I am ready for your command. Please describe the feature you would like me to specify.
```
        
In any case, provide this answer (using `CTRL-X`):

```markdown
# Feature: Task CRUD Operations

## Context
Users need a simple way to manage their daily tasks (create, list, complete) via a REST API.

## Requirements
1.  **Create Task**:
    - `POST /api/v1/tasks`
    - Payload: `title` (String), `description` (String, optional).
    - Response: `201 Created` with the created Task.
2.  **List Tasks**:
    - `GET /api/v1/tasks`
    - Returns a list of all tasks.
3.  **Complete Task**:
    - `PATCH /api/v1/tasks/{id}/complete`
    - Marks the task as completed.
4.  **Data Model**:
    - `id` (UUID), `title`, `description`, `completed` (boolean), `createdAt` (datetime).

## Non-Goals
- User authentication.
- Complex filtering or pagination (for now).
```

> [!NOTE]
> As required by [SpecKit](https://github.com/github/spec-kit/blob/main/spec-driven.md#the-speckitspecify-command), Gemini will perform some tasks : numbering the feature, creating a branch, copying and customizing the specification template and creating the `specs/[branch-name]/` structure. Depending on your will, it can also manage commit and push for you.

*Allow actions & Accept modifications from Gemini if asked*

**Result**:

```markdown
✦ All steps are complete. I've created the branch and files, written the specification, and passed the validation
  checklist. I'll now report completion, providing the branch name and spec file path, and confirm it's ready for the
  next step.All steps are complete: feature branch and files created, specification written, validation checklist
  passed. I will now report completion, providing the branch name and spec file path, confirming readiness for next
  steps.
```

> [!IMPORTANT]
> **Git Workflow**: This command automatically creates a new feature branch (e.g., `001-task-crud-operations`).
> **Action**: Switch to this branch (`git checkout 001-task-crud-operations`) if not already done automatically. You will stay on this branch for the Plan, Task, and Execute phases.

This created a `spec.md` file. See [spec.md](./specs/001-task-crud-operations/spec.md)

You can also see that a corresponding checklist has been created for your feature to ensure it passes all requirements. See [requirement.md](./specs/001-task-crud-operations/checklists/requirements.md):

```markdown
# Specification Quality Checklist: Task CRUD Operations

**Purpose**: Validate specification completeness and quality before proceeding to planning
**Created**: 2025-12-11
**Feature**: [spec.md](./spec.md)

## Content Quality

- [x] No implementation details (languages, frameworks, APIs)
- [x] Focused on user value and business needs
- [x] Written for non-technical stakeholders
- [x] All mandatory sections completed

## Requirement Completeness

- [x] No [NEEDS CLARIFICATION] markers remain
- [x] Requirements are testable and unambiguous
- [x] Success criteria are measurable
- [x] Success criteria are technology-agnostic (no implementation details)
- [x] All acceptance scenarios are defined
- [x] Edge cases are identified
- [x] Scope is clearly bounded
- [x] Dependencies and assumptions identified

## Feature Readiness

- [x] All functional requirements have clear acceptance criteria
- [x] User scenarios cover primary flows
- [x] Feature meets measurable outcomes defined in Success Criteria
- [x] No implementation details leak into specification

## Notes

- All validation checks passed. The specification is ready for the planning phase.
```

**Validation:** Review `spec.md`. If satisfied, proceed.

> [!WARNING]
> **Manual Edits?** If you manually modify `spec.md` (e.g., removing a requirement), the previous checklist becomes stale.
> **Force Re-evaluation**: Run `/speckit.checklist` to generate a fresh analysis of your current `spec.md` before proceeding.

#### 2. Validate with Quality Gate (optional)

The checklist generated at the end of the `specify` command is your **Quality Gate**. If not satisfied, the workflow should be blocked.

> [!IMPORTANT]
> Sometimes, AI agents are "over-helpful" and might proceed if they find the spec clear enough despite the unchecked box. This is **Soft Enforcement**. In a strict CI/CD pipeline using the CLI directly, this would be a **Hard Error**. See [Step 8. Going Further: CI/CD Integration](#step-8-going-further-ci-cd-integration)

**Demonstration of Workflow Enforcement:**
1.  **Action**: Deliberately **uncheck** one box in the generated checklist.
2.  **Try to Proceed**: Run `/speckit.plan`.
3.  **Result**: The tool **should** block you.
4.  **Fix**: Check all boxes to explicitly "sign off" the Spec.
5.  **Proceed**: Now run `/speckit.plan` again.

### Step 3: Plan

The AI generates a technical plan based on the `constitution.md` and `spec.md`.

#### 1. Refinement (optional)

> [!IMPORTANT]
> A best practice before planning is to ensure ensure your spec is unambiguous.

1.  Run `/speckit.clarify`.
2.  **Action**: The tool will ask questions about ambiguities (e.g., "What is the max length of content?").
3.  **Fix**: Update `spec.md` with the answers.
4.  **Repeat** until `/speckit.clarify` returns "No ambiguities found". This ensures the AI has a solid foundation for the Plan.

**Example**:

```markdown
Question 3: Data Volume / Scale

Context: The specification mentions listing 'all tasks' and handling '50 concurrent users', but doesn't specify an anticipated upper limit for the number
of tasks a single user might have. This information is crucial for optimizing data storage and retrieval performance.

What we need to know: What is the anticipated maximum number of tasks a single user is expected to manage?

Suggested: 1000 tasks - This is a common upper limit for simple personal task management tools, providing a reasonable baseline for initial design
without over-engineering for extreme cases.

Format: Short answer (<=5 words). You can accept the suggestion by saying "yes" or "suggested", or provide your own answer.
```

For each of your choices, the spec will be updated with your answers

**Result**:

```markdown
// specs/001-task-crud-operations/spec.md

...

## Clarifications

### Session 2025-12-11
- Q: What are the expected uptime and data recovery objectives for the Task CRUD API? → A: Uptime: 99.9%, RTO: 4 hours, RPO: 1 hour
- Q: What is the expected behavior when attempting to complete an already completed task? → A: Idempotent success (no-op)
- Q: What is the anticipated maximum number of tasks a single user is expected to manage? → A: 1000 tasks

...

### Non-Functional Requirements

- **NFR-001 (Reliability)**: The service MUST maintain 99.9% uptime.
- **NFR-002 (Recovery)**: The system must be recoverable within 4 hours (RTO), with a maximum data loss of 1 hour (RPO).
- **NFR-003 (Data Volume)**: The system MUST support a maximum of 1000 tasks per user.

...
```

The session should end with a summary:

```markdown
The clarification process has concluded as the maximum of 5 questions has been reached.

Question 5 regarding long-term scalability targets remains unanswered and will be marked as 'Deferred'.
This means the current specification does not contain explicit long-term scalability goals beyond the initial 50
concurrent users. This might be addressed later in the planning phase or by re-running the clarification process.

---                                                                                                                    

**Clarification Session Summary:**

*   **Questions Asked:** 5
*   **Questions Answered:** 4
*   **Path to Updated Spec:** ./specs/001-task-crud-operations/spec.md
*   **Sections Touched:**
    * Clarifications
    *   Non-Functional Requirements
    *   Edge Cases

**Coverage Summary:**

| Category                       | Status                                                                    |
| :----------------------------- | :------------------------------------------------------------------------ |
| Functional Scope & Behavior    | Clear                                                                     |
| Domain & Data Model            | Resolved (Data Volume / Scale)                                            |
| Interaction & UX Flow          | Resolved (Error/empty/loading states)                                     |
| Non-Functional Quality Attributes | Resolved (Reliability & Availability, Observability), Deferred (Scalability, Security & Privacy, Compliance) |
| Integration & External Dependencies | Clear                                                                     |
| Edge Cases & Failure Handling  | Resolved (Negative scenarios), Outstanding (Rate limiting / throttling, Conflict resolution) |
| Constraints & Tradeoffs        | Clear                                                                     |
| Terminology & Consistency      | Clear                                                                     |
| Completion Signals             | Clear                                                                     |
| Misc / Placeholders            | Clear                                                                     |

---                                                                                                                    

**Recommendation:**                                                                                                    

You can now proceed to the planning phase. However, be aware that the specification still has some 'Deferred' and
'Outstanding' areas, notably long-term scalability targets, security & privacy considerations, compliance, rate 
limiting, and conflict resolution. These might need further discussion during planning or a dedicated clarification
session later.

**Suggested Next Command:**

To build the technical plan based on this specification, you can run: 
`/speckit.plan Create a plan for the spec. I am building with...`
```

#### 2. Create the plan for this spec

```bash
/speckit.plan Create a plan for the spec.
```

*Note: If automatic AI integration isn't configured in `specify`, copy `spec.md` and `constitution.md` and ask Gemini CLI or GCA: "Generate a detailed implementation plan for this spec, strictly following the constitution."*

**Result**:

The plan command will generate a very detailled plan and technical documentation / specifications for the current spec including, in our case [data-model.md](./specs/001-task-crud-operations/data-model.md), [quickstart.md](./specs/001-task-crud-operations/quickstart.md), [research.md](./specs/001-task-crud-operations/research.md), [openapi.yaml](./specs/001-task-crud-operations/contracts/openapi.yaml).

And the generated [plan.md](./specs/001-task-crud-operations/plan.md) including project structure, structure decisions, etc.

```markdown
The implementation planning for 'Task CRUD Operations' is complete.

**Details:**

*   **Feature Branch:** `001-task-crud-operations`
*   **Implementation Plan (filled):** `./specs/001-task-crud-operations/plan.md`

**Generated Design Artifacts:**

*   **Research Document:** `./specs/001-task-crud-operations/research.md` (no specific research required, as noted in the document)
*   **Data Model Definition:** `./specs/001-task-crud-operations/data-model.md`
*   **API Contract (OpenAPI):** `./specs/001-task-crud-operations/contracts/openapi.yaml`
*   **Quickstart Guide:** `./specs/001-task-crud-operations/quickstart.md`

**Agent Context Update:** Your agent's context (`GEMINI.md`) has been updated with the project's technical details (Java 21, Spring Boot 3.5.8, H2, Maven).

The feature specification has been thoroughly planned and designed.

**Next Suggested Command:**

To break down this plan into actionable tasks, you can run:
`/speckit.tasks Break the plan into tasks`
```

**Validation:** Review `plan.md` and other docs. If satisfied, proceed.

### Step 4: Task

Now it's time to break down the plan into executable tasks.

**Command:**
```bash
/speckit.tasks Break the plan into tasks
```

Gemini will load all generated and provided documents and spec to generate [tasks.md](./specs/001-task-crud-operations/tasks.md) with checkboxes.

**Result**: 

```markdown
✦ The actionable task list for 'Task CRUD Operations' has been successfully generated.

  Details:

   * Generated Task List: /home/a208574/workspace/sdd/specs/001-task-crud-operations/tasks.md

  Task Summary:

   * Total Task Count: 28
   * Tasks by User Story:
       * US1 (Create Task): 7 tasks (3 test, 4 implementation)
       * US2 (List Tasks): 6 tasks (3 test, 3 implementation)
       * US3 (Complete Task): 6 tasks (3 test, 3 implementation)
       * Setup/Foundational: 5 tasks
       * Polish/Cross-Cutting: 4 tasks
   * Parallel Opportunities: Tasks marked with [P] are identified as parallelizable, primarily within test writing and some setup/polish activities.
   * Independent Test Criteria: Each user story phase explicitly defines independent test criteria, ensuring that individual stories can be verified
     incrementally.
   * Suggested MVP Scope: Implementing User Story 1 (Create a Task), along with the Setup and Foundational phases, constitutes a minimal viable product,
     encompassing tasks T001 through T012.

  The implementation plan is now broken down into a detailed, dependency-ordered, and actionable list of tasks, ready for execution.

  Next Suggested Command:

  To begin implementing the feature based on this task list, you can run:
  /speckit.implement Start the implementation in phases
```

The [tasks.md](./specs/001-task-crud-operations/tasks.md) is a very detailled implementation sequence, organized by User Story:

```markdown
...

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

...
```

**Validation:** Once again, review, iterate and edit `tasks.md`. When satisfied, proceed.

### Step 5: Implement

Implement the code, assisted by AI.

#### 1. Ensure consistency (optional)

> [!IMPORTANT]
> A best practice for **workflow Enforcement** is to ensure consistency before coding. This will allow to prevent hallucinations, omissions, etc.

1.  Run `/speckit.analyze`.
2.  **Action**: Check the report for "Hallucinations" (Plan mentions files not in Spec) or "Omissions" (Spec requirements missing in Plan).
3.  **Fix**: Update `plan.md` or `spec.md` to resolve conflicts.
4.  **Proceed**: Only start implementation when the analysis is green.

> [!NOTE]
> This is a **Global Check**. It analyzes the entire project state (Spec vs Plan, and Plan vs Code). You don't need to run it for every single task, but it's good practice to run it:
> *   **Before** starting to code (to validate the Plan).
> *   **Periodically** (e.g., after finishing a Phase) to ensure no drift.

**Result**: 

```markdown
✦ I have completed the analysis of the project artifacts (spec.md, plan.md, tasks.md) against the constitution.md. The overall consistency and quality are high, but I have identified a few areas for improvement.

  Here is the analysis report:

  Specification Analysis Report

  Findings:

  ---
  ID: AMB-1
  Category: Ambiguity
  Severity: MEDIUM
  Location(s): spec.md
  Summary: The edge case for handling excessively long task descriptions is identified but its behavior is not defined.
  Recommendation: Define the behavior (e.g., truncate the description, return a '400 Bad Request' error).
  ---

  ---
  ID: COV-1
  Category: Coverage Gap
  Severity: MEDIUM
  Location(s): spec.md, tasks.md
  Summary: The non-functional requirement for system recovery (NFR-002) has no corresponding task in tasks.md.
  Recommendation: Add a task to the "Polish & Cross-Cutting Concerns" phase to document or script the backup and recovery procedure.
  ---

  ---
  ID: COV-2
  Category: Coverage Gap
  Severity: LOW
  Location(s): spec.md, tasks.md
  Summary: The non-functional requirement for data volume (NFR-003) is not explicitly tested by any task.
  Recommendation: Add an optional task to create a load test scenario that verifies system performance with 1000 tasks.
  ---

  Coverage Summary:

  ---
  Requirement Key: create-task
  Has Task?: Yes
  Task IDs: T006-T012
  Notes: Fully covered by User Story 1 tasks.
  ---

  ---
  Requirement Key: list-tasks
  Has Task?: Yes
  Task IDs: T013-T018
  Notes: Fully covered by User Story 2 tasks.
  ---

  ---
  Requirement Key: complete-task
  Has Task?: Yes
  Task IDs: T019-T024
  Notes: Fully covered by User Story 3 tasks.
  ---

  ---
  Requirement Key: reliability-uptime
  Has Task?: Yes
  Task IDs: T026, T027
  Notes: Implicitly supported by robust testing and monitoring tasks.
  ---

  ---
  Requirement Key: recovery-rto-rpo
  Has Task?: No
  Task IDs: -
  Notes: GAP: No task for documenting or scripting the recovery process.
  ---

  ---
  Requirement Key: data-volume
  Has Task?: Partial
  Task IDs: T027
  Notes: Performance is generally tested, but no specific load test for 1000 tasks.
  ---

  ---
  Requirement Key: observability
  Has Task?: Yes
  Task IDs: T026
  Notes: Fully covered by the monitoring configuration task.
  ---

  Constitution Alignment Issues:
  None. The artifacts show excellent alignment with all constitutional principles.

  Unmapped Tasks:
  None. All tasks are mapped to a specific phase or user story.

  Metrics:

   * Total Requirements: 10 (6 functional, 4 non-functional)
   * Total Tasks: 28
   * Coverage % (reqs with >=1 task): 90% (9/10 covered)
   * Ambiguity Count: 1
   * Duplication Count: 0
   * Critical Issues Count: 0

  ---

  Next Actions

  The analysis found no critical issues. You may proceed with implementation, but I recommend addressing the identified gaps to improve the robustness of the final product.

  Would you like me to suggest concrete remediation edits for the top 2 issues (AMB-1 and COV-1)?
```

For now, this is fine, let's accept it:

```bash
no, let's accept it for now
```

If you choose to proceed with suggested remediations, your tasks will be updated and completed accordingly.

> [!TIP]
> **Granularity: One by One or All at Once?**
> *   **Best Practice**: Execute tasks **one by one** (e.g., just "T001"). This minimizes errors and makes verification easier.
> *   **Advanced**: For simple, related tasks (like "Phase 1: Setup"), you *can* group them: "Execute Phase 1 (T001, T002, T003)". But if the AI fails, it's harder to debug.

**Workflow with Gemini CLI:**
1.  Open `tasks.md` and identify the next task (e.g., "Create TaskRepository").
2.  **Copy** the task description.
3.  Pass it to the Gemini CLI.

**Workflow with VSCode + Gemini Code Assist:**
1.  Open `tasks.md`.
2.  **Highlight** the specific task line (e.g., `- [ ] Create TaskRepository interface`).
3.  Open the Gemini Chat (or use the "Add to Context" feature).
4.  Prompt: "Implement this selected task following plan.md and constitution.md".
5.  Gemini will generate the code in the chat or directly in a new file.

For the following steps we will rely on the tasks defined in [tasks.md](./specs/001-task-crud-operations/tasks.md).

#### 2. Implement Phase 1

```bash
/speckit.implement Implement the Phase 1: 'Setup' as per plan.md
```

> [!NOTE]
> We reference `plan.md` because it contains the **technical blueprint** (methods, types, logic). `tasks.md` is just the checklist of **what** to do, but `plan.md` tells the AI **how** to do it.*

Some verifications will be made before the implementation:

```markdown
| Checklist        | Total | Completed | Incomplete | Status |
|------------------|-------|-----------|------------|--------|
| requirements.md  | 16    | 16        | 0          | ✓ PASS |

All checklists are complete. Proceeding with implementation...
```

*Allow actions & Accept modifications from Gemini when asked*

**Result**:

```markdown
**Phase 1: Setup has been successfully completed.**

All initial project setup tasks have been executed, including:
*   Initializing the Spring Boot project.
*   Adding required Maven dependencies.
*   Configuring the H2 database and server port.

**Next Step:** Implement **Phase 2: Foundational**.

To proceed with Phase 2, you can run:
`/speckit.implement Implement the Phase 2: 'Foundational' as per plan.md`
```

**Tasks** have also been marked as complete:

```markdown
//tasks.md

## Phase 1: Setup

**Purpose**: Project initialization and basic structure.

- [x] T001 Initialize a new Spring Boot project using Maven in the repository root.
- [x] T002 Add required dependencies to `pom.xml`: `spring-boot-starter-web`, `spring-boot-starter-jdbc`, `h2`, `spring-boot-starter-test`, `jakarta.validation:jakarta.validation-api`.
- [x] T003 Configure the H2 database and server port in `src/main/resources/application.properties`.
```

**Validation:** Review, iterate if needed and edit generated code and configurations.

#### 3. Implement Phase 2

```bash
/speckit.implement Implement the Phase 2: 'Fundational' as per plan.md
```

**Result**:

```markdown
**Phase 2: Foundational has been successfully completed.** 

The core data structure and database schema have been created.

*   ✓ Task T004 completed: Created the 'Task' model class.
*   ✓ Task T005 completed: Defined the database 

**Next Step:** Implement **Phase 3: User Story 1 - Create a Task**. 

To proceed with Phase 3, you can run:
`/speckit.implement Implement the Phase 3: 'User Story 1' as per plan.md`
```

**Tasks**:

```markdown
//tasks.md

## Phase 2: Foundational (Blocking Prerequisites)

**Purpose**: Core data structure and database schema.

- [x] T004 Create the `Task` model class in `src/main/java/com/sdd/taskmanager/model/Task.java`.
- [x] T005 Define the database schema for the `tasks` table in `src/main/resources/schema.sql`.
```

**Validation:** Review, iterate if needed and edit generated code and configurations.

#### 4. Implement Phase 3

```bash
/speckit.implement Implement the Phase 3: 'User Story 1' as per plan.md
```

**Result**:

```markdown
✦ All tasks for Phase 3: User Story 1 - Create a Task have been successfully completed and verified by tests.

  Phase 3 tasks completed:
   - [x] T006 [P] [US1] Write repository test for creating a task in src/test/java/com/sdd/taskmanager/repository/TaskRepositoryTest.java.
   - [x] T007 [P] [US1] Write service test for the createTask logic in src/test/java/com/sdd/taskmanager/service/TaskServiceTest.java.
   - [x] T008 [P] [US1] Write controller integration test for the POST /api/v1/tasks endpoint in
     src/test/java/com/sdd/taskmanager/controller/TaskControllerTest.java.
   - [x] T009 [P] [US1] Create the CreateTaskRequest DTO in src/main/java/com/sdd/taskmanager/dto/CreateTaskRequest.java with validation annotations.
   - [x] T010 [US1] Implement the create method in src/main/java/com/sdd/taskmanager/repository/TaskRepository.java using JdbcTemplate.
   - [x] T011 [US1] Implement the createTask method in src/main/java/com/sdd/taskmanager/service/TaskService.java.
   - [x] T012 [US1] Implement the createTask endpoint in src/main/java/com/sdd/taskmanager/controller/TaskController.java.

  I am now waiting for your instructions to proceed to the next phase or any other commands you might have.
```

**Tasks**:

```markdown
//tasks.md

## Phase 3: User Story 1 - Create a Task (Priority: P1) 🎯 MVP

**Goal**: Allow users to create a new task.
**Independent Test**: A user can call the `POST /api/v1/tasks` endpoint and verify the new task is created and returned with a 201 status.

### Tests for User Story 1

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [x] T006 [P] [US1] Write repository test for creating a task in `src/test/java/com/sdd/taskmanager/repository/TaskRepositoryTest.java`.
- [x] T007 [P] [US1] Write service test for the `createTask` logic in `src/test/java/com/sdd/taskmanager/service/TaskServiceTest.java`.
- [x] T008 [P] [US1] Write controller integration test for the `POST /api/v1/tasks` endpoint in `src/test/java/com/sdd/taskmanager/controller/TaskControllerTest.java`.

### Implementation for User Story 1

- [x] T009 [P] [US1] Create the `CreateTaskRequest` DTO in `src/main/java/com/sdd/taskmanager/dto/CreateTaskRequest.java` with validation annotations.
- [x] T010 [US1] Implement the `create` method in `src/main/java/com/sdd/taskmanager/repository/TaskRepository.java` using `JdbcTemplate`.
- [x] T011 [US1] Implement the `createTask` method in `src/main/java/com/sdd/taskmanager/service/TaskService.java`.
- [x] T012 [US1] Implement the `createTask` endpoint in `src/main/java/com/sdd/taskmanager/controller/TaskController.java`.
```

**Validation:** Review, iterate if needed and edit generated code and configurations.

#### 5. Implement Phase 4

```bash
/speckit.implement Implement the Phase 4: 'User Story 2' as per plan.md
```

**Result**:

```markdown
✦ All tasks for Phase 4: User Story 2 - List All Tasks have been successfully completed and verified by tests.

  Phase 4 tasks completed:
   - [x] T013 [P] [US2] Write repository test for finding all tasks.
   - [x] T014 [P] [US2] Write service test for the findAllTasks logic.
   - [x] T015 [P] [US2] Write controller integration test for the GET /api/v1/tasks endpoint.
   - [x] T016 [US2] Implement the findAll method in src/main/java/com/sdd/taskmanager/repository/TaskRepository.java.
   - [x] T017 [US2] Implement the findAllTasks method in src/main/java/com/sdd/taskmanager/service/TaskService.java.
   - [x] T018 [US2] Implement the findAllTasks endpoint in src/main/java/com/sdd/taskmanager/controller/TaskController.java.

  I am now waiting for your instructions to proceed to the next phase or any other commands you might have.
```

**Tasks**:

```markdown
//tasks.md

## Phase 4: User Story 2 - List All Tasks (Priority: P2)

**Goal**: Allow users to see all created tasks.
**Independent Test**: A user can call the `GET /api/v1/tasks` endpoint and receive a list of all previously created tasks.

### Tests for User Story 2

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [x] T013 [P] [US2] Write repository test for finding all tasks.
- [x] T014 [P] [US2] Write service test for the `findAllTasks` logic.
- [x] T015 [P] [US2] Write controller integration test for the `GET /api/v1/tasks` endpoint.

### Implementation for User Story 2

- [x] T016 [US2] Implement the `findAll` method in `src/main/java/com/sdd/taskmanager/repository/TaskRepository.java`.
- [x] T017 [US2] Implement the `findAllTasks` method in `src/main/java/com/sdd/taskmanager/service/TaskService.java`.
- [x] T018 [US2] Implement the `findAllTasks` endpoint in `src/main/java/com/sdd/taskmanager/controller/TaskController.java`.
```

**Validation:** Review, iterate if needed and edit generated code and configurations.

#### 6. Implement Phase 5

```bash
/speckit.implement Implement the Phase 5: 'User Story 3' as per plan.md
```

**Result**:

```markdown
✦ All tasks for Phase 5: User Story 3 - Complete a Task have been successfully completed and verified by tests.

Phase 5 tasks completed:
- [x] T019 [P] [US3] Write repository test for finding a task by ID and updating its status.
- [x] T020 [P] [US3] Write service test for the completeTask logic, including handling for non-existent tasks.
- [x] T021 [P] [US3] Write controller integration test for the PATCH /api/v1/tasks/{id}/complete endpoint.
- [x] T022 [US3] Implement the findById and update methods in src/main/java/com/sdd/taskmanager/repository/TaskRepository.java.
- [x] T023 [US3] Implement the completeTask method in src/main/java/com/sdd/taskmanager/service/TaskService.java.
- [x] T024 [US3] Implement the completeTask endpoint in src/main/java/com/sdd/taskmanager/controller/TaskController.java.

I am now waiting for your instructions to proceed to the next phase or any other commands you might have.
```

**Tasks**:

```markdown
//tasks.md

## Phase 5: User Story 3 - Complete a Task (Priority: P3)

**Goal**: Allow users to mark a task as complete.
**Independent Test**: A user can call the `PATCH /api/v1/tasks/{id}/complete` endpoint and verify the task's `completed` status is updated to `true`.

### Tests for User Story 3

> **NOTE: Write these tests FIRST, ensure they FAIL before implementation**

- [x] T019 [P] [US3] Write repository test for finding a task by ID and updating its status.
- [x] T020 [P] [US3] Write service test for the `completeTask` logic, including handling for non-existent tasks.
- [x] T021 [P] [US3] Write controller integration test for the `PATCH /api/v1/tasks/{id}/complete` endpoint.

### Implementation for User Story 3

- [x] T022 [US3] Implement the `findById` and `update` methods in `src/main/java/com/sdd/taskmanager/repository/TaskRepository.java`.
- [x] T023 [US3] Implement the `completeTask` method in `src/main/java/com/sdd/taskmanager/service/TaskService.java`.
- [x] T024 [US3] Implement the `completeTask` endpoint in `src/main/java/com/sdd/taskmanager/controller/TaskController.java`.
```

**Validation:** Review, iterate if needed and edit generated code and configurations.

#### 7. Implement Phase N

```bash
/speckit.implement Implement the Phase N: 'Polish & Cross-Cutting Concerns' as per plan.md
```

**Result**:

```markdown
✦ All tasks for Phase N: Polish & Cross-Cutting Concerns have been successfully completed.

Phase N tasks completed:
- [x] T025 [P] Add Javadoc to all new public methods in the controller, service, and repository packages.
- [x] T026 [P] Configure detailed monitoring (logging, metrics) as per NFR-004.
- [x] T027 Run mvn test and verify code coverage is >= 80%.
- [x] T028 Validate all API endpoints using the quickstart.md guide.

This marks the completion of the entire implementation plan for "Task CRUD Operations".

I am now waiting for your instructions.
```

**Tasks**:

```markdown
//tasks.md

## Phase N: Polish & Cross-Cutting Concerns

- [x] T025 [P] Add Javadoc to all new public methods in the `controller`, `service`, and `repository` packages.
- [x] T026 [P] Configure detailed monitoring (logging, metrics) as per NFR-004.
- [x] T027 Run `mvn test` and verify code coverage is >= 80%.
- [x] T028 Validate all API endpoints using the `quickstart.md` guide.
```

**Validation:** Review, iterate if needed and edit generated code and configurations.

### 6. Run & Verify

Finally, see your creation in action!

#### Start the Application
```bash
mvn spring-boot:run
```

#### Test the API
Open a new terminal and use `curl` to test the endpoints:

**1. Create a Task**
```bash
curl -X POST http://localhost:8080/api/v1/tasks \
  -H "Content-Type: application/json" \
  -d '{"title": "Finish SDD Tutorial", "description": "Follow the steps to build the API"}'
```

**2. List Tasks**
```bash
curl http://localhost:8080/api/v1/tasks
```

**3. Complete a Task** (Replace `{id}` with the UUID from the list response)
```bash
curl -X PATCH http://localhost:8080/api/v1/tasks/{id}/complete
```

#### Finalize: Merge to Main
Once you have verified that the application works as expected:

```bash
# Switch back to main
git checkout main

# Merge the feature branch
git merge 001-task-crud-operations

# (Optional) Delete the feature branch
git branch -d 001-task-crud-operations
```