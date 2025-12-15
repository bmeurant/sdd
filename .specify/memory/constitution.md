<!--
Sync Impact Report:
- Version change: 0.0.0 → 1.0.0
- Added sections:
  - I. Tech Stack
  - II. Architectural Constraints (Strict)
  - III. Coding Standards
- Templates requiring updates:
  - ⏳ .specify/templates/plan-template.md
  - ⏳ .specify/templates/spec-template.md
  - ⏳ .specify/templates/tasks-template.md
-->
# Project Constitution: Task Manager API

## Core Principles

### I. Tech Stack
- **Language**: Java 21
- **Framework**: Spring Boot 3.5.8
- **Database**: H2 (In-Memory)
- **Build Tool**: Maven

### II. Architectural Constraints (Strict)
- **No ORMs**: Strictly forbidden (Hibernate, JPA, etc.). Use **JdbcTemplate** exclusively for database access.
- **Layered Architecture**: Controller -> Service -> Repository.
- **Validation**: Use `jakarta.validation` constraints on DTOs.

### III. Coding Standards
- **Testing**: JUnit 5 and Mockito. **Minimum 80% code coverage** enforced.
- **Documentation**: Javadoc on all public methods.
- **Style**: Google Java Style Guide.
- **Commits**: Follow **Conventional Commits** (e.g., `feat: add task endpoint`).

## Governance
This Constitution is the single source of truth for project standards. All development, reviews, and tooling must align with these principles. Amendments require documented justification, review, and version increment according to Semantic Versioning.

**Version**: 1.0.0 | **Ratified**: 2025-12-11 | **Last Amended**: 2025-12-11