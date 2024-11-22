# RegistrationRepository Documentation

## Purpose

This interface represents the `RegistrationRepository`, which provides methods to perform CRUD operations and custom queries for managing `Registration` entities in the database.

## Inheritance

- **JpaRepository<Registration, Long>**: Extends the Spring Data JPA repository interface to provide standard CRUD operations for the `Registration` entity.

## Custom Query Methods

- **findCoursesByStudentNumber(@Param("studentNumber") Long studentNumber)**: Retrieves a list of courses that a specific student is enrolled in, using their student number.
- **findStudentsByCourseId(@Param("courseId") Long courseId)**: Retrieves a list of students enrolled in a specific course, using the course ID.

## Annotations

- **@Repository**: Marks this interface as a Spring Data repository, allowing it to be detected during component scanning and instantiated as a bean by Spring.
- **@Query**: Specifies the custom queries used to join the `Registration` table with the `Course` or `Student` entities to find courses by student number or students by course ID.
- **@Param**: Binds the method parameters to the named parameters in the custom queries.

## Usage

This repository is used to manage `Registration` entities, providing methods for both basic CRUD operations and custom queries. The custom query methods allow for retrieving courses by student number and students by course ID, which are essential for managing student enrollment and course participation data.

