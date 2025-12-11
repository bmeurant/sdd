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