# GradeRepository Documentation

## Purpose

This interface represents the `GradeRepository`, which provides methods to perform CRUD operations and custom queries for managing `Grade` entities in the database.

## Inheritance

- **JpaRepository<Grade, Integer>**: Extends the Spring Data JPA repository interface to provide standard CRUD operations for the `Grade` entity.

## Custom Query Methods

- **findByStudentNumber(Long studentNumber)**: Retrieves a list of grades for a specific student using their student number.
- **findByCourse_CourseId(Long courseId)**: Retrieves a list of grades for a specific course using the course ID.

## Annotations

- **@Repository**: Marks this interface as a Spring Data repository, allowing it to be detected during component scanning and instantiated as a bean by Spring.

## Usage

This repository is used to manage `Grade` entities, providing methods for both basic CRUD operations and custom queries. The custom query methods allow for retrieving grades by student number and course ID, which are essential for managing student performance and academic records.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)