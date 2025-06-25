---
title: "Fix JPA Entity Imports (javax -> jakarta)"
type: "task"
status: "completed"
created: "2025-06-25T22:00:00"
updated: "2025-01-27T12:41:19"
id: "TASK-FIX-001"
priority: "highest"
memory_types: ["procedural"]
dependencies: []
tags: ["backend", "fix", "jpa", "blocker"]
---

# TASK-FIX-001: Fix JPA Entity Imports (javax -> jakarta)

## Description
This is a high-priority task to resolve a critical compilation blocker. The project's entities are currently using `javax.persistence` annotations, which are incompatible with Spring Boot 3.x and cause the build to fail. This task involves updating all entity classes to use the correct `jakarta.persistence` namespace.

## Objectives
- Make the project compilable.
- Unblock development on all other backend tasks (e.g., repository, service, controller layers).
- Ensure all JPA entities are aligned with the modern Spring framework standards.

## Steps & Progress
1.  [x] **Locate all entity files**:
    -   [x] `CartItem.java`
    -   [x] `Category.java`
    -   [x] `Order.java`
    -   [x] `OrderItem.java`
    -   [x] `PaymentMethod.java`
    -   [x] `Product.java`
    -   [x] `Review.java`
    -   [x] `Sale.java`
    -   [x] `SaleDetail.java`
    -   [x] `User.java`
2.  [x] **For each entity file**:
    -   [x] Replace `import javax.persistence.*;` with `import jakarta.persistence.*;`.
3.  [x] **Verify the fix**:
    -   [x] Run `./gradlew clean build` or `./gradlew compileJava` to confirm that all compilation errors related to persistence are resolved.

## Dependencies
- None. This task is a root dependency for all other backend tasks.

## Notes
- This task has been completed successfully. All entity files now use `jakarta.persistence` imports.
- Compilation test passed with `BUILD SUCCESSFUL`.
- Added `jakarta.persistence:jakarta.persistence-api:3.1.0` dependency to build.gradle for explicit support.

## Next Steps
- Task is complete and ready to be moved to completed status.
- Can now proceed with the original plan, starting with `TASK-HP-004` (Repository). 