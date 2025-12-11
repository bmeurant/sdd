# Data Model: Task CRUD Operations

This document describes the data entities for the Task CRUD Operations feature, based on the requirements in the feature specification.

## Task Entity

Represents a single to-do item.

**Fields:**

| Field         | Type      | Description                                     | Constraints / Validation Rules                               |
|---------------|-----------|-------------------------------------------------|----------------------------------------------------------------|
| `id`          | `UUID`    | The unique identifier for the task.             | Primary Key, auto-generated.                                   |
| `title`       | `String`  | The title or name of the task.                  | Mandatory, non-empty. Max length: 255 characters (suggested).  |
| `description` | `String`  | An optional, longer description of the task.    | Optional. Max length: 1000 characters (suggested).             |
| `completed`   | `boolean` | The completion status of the task.              | Mandatory. Defaults to `false` on creation.                    |
| `createdAt`   | `ZonedDateTime` | The exact date and time the task was created. | Mandatory, auto-generated on creation. Stored in UTC.        |

**State Transitions:**

- A `Task` is created with `completed` status as `false`.
- The `completed` status can transition from `false` to `true`.
- The transition from `true` back to `false` (un-completing) is not a defined requirement for this feature.
