---
title: "Implement Product Service Logic"
type: "task"
status: "completed"
created: "2025-06-25T21:17:00"
updated: "2025-01-27T12:50:00"
id: "TASK-HP-003"
priority: "high"
memory_types: ["procedural"]
dependencies: ["TASK-HP-004"]
tags: ["backend", "api", "service"]
---

# TASK-HP-003: Implement Product Service Logic

## Description
This task focuses on creating the `ProductService` which will contain the core business logic for fetching products. It will take parameters from the controller, build a dynamic query using `JpaSpecificationExecutor`, and retrieve the data from the `ProductRepository`.

## Objectives
- Create a `ProductService` interface and its implementation `ProductServiceImpl`.
- Define a method `findAll(keyword, categoryId, pageable)` that returns a `Page<Product>`.
- Dynamically build a `Specification<Product>` based on the provided `keyword` and `categoryId`.
- If `keyword` is present, add a `LIKE` condition on the product's `name` field.
- If `categoryId` is present, add an `EQUAL` condition on the product's `categoryID` field.
- Call the repository's `findAll(specification, pageable)` method.

## Steps
1. Create `ProductService.java` interface in `src/main/java/com/java6/datn/service/`.
2. Create `ProductServiceImpl.java` implementing the interface in `src/main/java/com/java6/datn/service/impl/`.
3. Inject `ProductRepository` into the service implementation.
4. Implement the `findAll` method in the service.
5. Inside `findAll`, create an instance of `Specification<Product>`.
6. Add the `keyword` search predicate to the specification if the keyword is not null or empty.
7. Add the `categoryId` filter predicate to the specification if the ID is not null.
8. Execute the query using `productRepository.findAll(spec, pageable)` and return the result.

## Progress
- [x] Step 1: Interface created
- [x] Step 2: Implementation class created
- [x] Step 3: Repository injected
- [x] Step 4: `findAll` method implemented
- [x] Step 5: Specification instantiated
- [x] Step 6: Keyword predicate added
- [x] Step 7: Category predicate added
- [x] Step 8: Query executed and result returned

## Dependencies
- `ProductRepository` with `JpaSpecificationExecutor` (TASK-HP-004)

## Notes
- The use of a separate `ProductSpecification` class is recommended to keep the service layer clean. This will be handled in TASK-HP-004.
- Remember to handle the case where both `keyword` and `categoryId` are null, which should return a simple paginated list of all products.
- **COMPLETED**: ProductService interface created with comprehensive method signatures.
- **COMPLETED**: ProductServiceImpl created with proper dependency injection using @Autowired.
- **COMPLETED**: Dynamic query building using ProductSpecification.getSpecs() method.
- **COMPLETED**: All filtering scenarios handled (keyword only, category only, both, neither).
- **BUILD STATUS**: Compilation successful.

## Next Steps
- Task is complete and ready to be moved to completed status.
- Integrate the service with the controller from TASK-HP-002. 