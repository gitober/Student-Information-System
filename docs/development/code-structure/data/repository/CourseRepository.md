# CourseRepository Documentation

## Purpose

This interface represents the `CourseRepository`, which provides methods to perform CRUD operations and custom queries for managing `Course` entities in the database.

## Inheritance

- **JpaRepository<Course, Long>**: Extends the Spring Data JPA repository interface to provide standard CRUD operations for the `Course` entity.

## Custom Query Methods

- **findCoursesByStudentId(@Param("studentId") Long studentId)**: Retrieves a list of courses for a specific student using a custom query that joins the `Course` and `Registration` entities to find courses by student ID.

## Annotations

- **@Repository**: Marks this interface as a Spring Data repository, allowing it to be detected during component scanning and instantiated as a bean by Spring.
- **@Query**: Specifies the custom query used to join the `Course` and `Registration` tables to find courses by student ID.
- **@Param("studentId")**: Binds the method parameter to the named parameter in the query.

## Usage

This repository is used to manage `Course` entities, providing methods for both basic CRUD operations and custom queries. The custom query method allows for retrieving courses by a specific student, which is useful for managing course enrollments and student-specific data in the application.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)