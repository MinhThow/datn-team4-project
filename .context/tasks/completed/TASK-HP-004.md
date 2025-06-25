---
title: "Implement Dynamic Product Repository & Specification"
type: "task"
status: "completed"
created: "2025-06-25T21:20:00"
updated: "2025-01-27T12:45:00"
id: "TASK-HP-004"
priority: "high"
memory_types: ["procedural"]
dependencies: []
tags: ["backend", "api", "repository", "jpa"]
---

# TASK-HP-04: Implement Dynamic Product Repository & Specification

## Description
This task involves modifying the `ProductRepository` to support dynamic queries and creating a dedicated `ProductSpecification` class. This will decouple the query-building logic from the service layer, resulting in cleaner and more maintainable code.

## Objectives
- Update the `ProductRepository` interface to extend `JpaSpecificationExecutor<Product>`.
- Create a new class `ProductSpecification` in a suitable package (e.g., `com.java6.datn.repository.specification`).
- Create a static method in `ProductSpecification` that returns a `Specification<Product>` for searching by `keyword`.
- Create another static method for filtering by `categoryId`.
- Create a main static method to combine these specifications.

## Steps
1. Locate the `ProductRepository.java` interface.
2. Modify its definition to extend `JpaRepository<Product, Integer>` and `JpaSpecificationExecutor<Product>`.
3. Create the package `com.java6.datn.repository.specification`.
4. Create the file `ProductSpecification.java` inside the new package.
5. Implement a static method `hasKeyword(String keyword)` that returns a `Specification<Product>` for a `LIKE` query on the `name` field.
6. Implement a static method `hasCategory(Integer categoryId)` that returns a `Specification<Product>` for an `EQUAL` query on the `categoryID` field.
7. Implement a static method `getSpecs(String keyword, Integer categoryId)` that combines the individual specifications using `Specification.where().and()`.

## Progress
- [x] Step 1: Repository located
- [x] Step 2: Repository updated to extend `JpaSpecificationExecutor`
- [x] Step 3: Specification package created
- [x] Step 4: `ProductSpecification` class created
- [x] Step 5: `hasKeyword` method implemented
- [x] Step 6: `hasCategory` method implemented
- [x] Step 7: `getSpecs` combination method implemented

## Dependencies
- None

## Notes
- This approach makes the `ProductService` much cleaner, as it can now simply call `ProductSpecification.getSpecs(...)` and pass the result to the repository.
- Pay attention to null-checks within the specification methods to avoid `NullPointerException`.
- **COMPLETED**: All specification methods have been implemented with proper null-checking.
- **COMPLETED**: Updated to use modern Specification API (removed deprecated `Specification.where()`).
- **BUILD STATUS**: Compilation successful with no warnings.

## Next Steps
- Task is complete and ready to be moved to completed status.
- Integrate this specification with the `ProductService` in TASK-HP-003. 