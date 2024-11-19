# StudentRepository Documentation

## Purpose

This interface represents the `StudentRepository`, which provides methods to perform CRUD operations, custom queries, and specification-based searches for managing `Student` entities in the database.

## Inheritance

- **JpaRepository<Student, Long>**: Extends the Spring Data JPA repository interface to provide standard CRUD operations for the `Student` entity.
- **JpaSpecificationExecutor<Student>**: Extends the Spring Data JPA specification executor interface to provide support for complex queries using specifications.

## Custom Query Methods

- **findByUsername(String username)**: Retrieves a student by their username.
- **findByCourses_CourseId(Long courseId)**: Retrieves a list of students enrolled in a specific course using the course ID.
- **findByStudentNumber(Long studentNumber)**: Retrieves an optional student by their student number.

## Usage

This repository is used to manage `Student` entities, providing methods for both basic CRUD operations, custom queries, and complex specification-based searches. The custom query methods allow for retrieving students by username, course enrollment, and student number, which are essential for managing student information and their participation in courses.

