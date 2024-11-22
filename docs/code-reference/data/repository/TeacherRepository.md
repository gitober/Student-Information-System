# TeacherRepository Documentation

## Purpose

This interface represents the `TeacherRepository`, which provides methods to perform CRUD operations, custom queries, and specification-based searches for managing `Teacher` entities in the database.

## Inheritance

- **JpaRepository<Teacher, Long>**: Extends the Spring Data JPA repository interface to provide standard CRUD operations for the `Teacher` entity.
- **JpaSpecificationExecutor<Teacher>**: Extends the Spring Data JPA specification executor interface to provide support for complex queries using specifications.

## Custom Query Methods

- **findById(Long id)**: Retrieves an optional teacher by their ID.
- **findByUsername(String username)**: Retrieves an optional teacher by their username.
- **findByDepartment_Name(String departmentName)**: Retrieves a list of teachers by their department name.
- **findBySubject_Name(String subjectName)**: Retrieves a list of teachers by their subject name.
- **findByDepartment_NameAndSubject_Name(String departmentName, String subjectName)**: Retrieves a list of teachers by both department name and subject name.
- **findTeacherWithCourses(Long id)**: Retrieves a teacher along with their courses eagerly loaded using a custom query.
- **findTeacherByUsernameWithCourses(@Param("username") String username)**: Retrieves an optional teacher by username along with their courses eagerly loaded using a custom query.

## Usage

This repository is used to manage `Teacher` entities, providing methods for both basic CRUD operations, custom queries, and complex specification-based searches. The custom query methods allow for retrieving teachers by their department, subject, or specific attributes, and also support eager fetching of associated courses, which is useful for managing teacher information efficiently and reducing the number of database queries.

---

[Back to System Overview](../../system-overview.md)
