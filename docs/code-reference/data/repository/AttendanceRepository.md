# AttendanceRepository Documentation

## Purpose

This interface represents the `AttendanceRepository`, which provides methods to perform CRUD operations and custom queries for managing `Attendance` entities in the database.

## Inheritance

- **JpaRepository<Attendance, Long>**: Extends the Spring Data JPA repository interface to provide standard CRUD operations for the `Attendance` entity.

## Custom Query Methods

- **findByStudent_StudentNumberAndCourse_CourseId(Long studentNumber, Long courseId)**: Retrieves a list of attendance records for a specific student and course.
- **findByCourse_CourseId(Long courseId)**: Retrieves a list of attendance records for a specific course based on the course ID.
- **findByCourseIn(List<Course> courses)**: Retrieves attendance records for a list of courses based on the provided course list.

## Annotations

- **@Repository**: Marks this interface as a Spring Data repository, allowing it to be detected during component scanning and instantiated as a bean by Spring.

## Usage

This repository is used to manage `Attendance` entities, providing methods for both basic CRUD operations and custom queries. The custom query methods allow for more specific lookups, such as retrieving attendance records by student number or course details, which are essential for managing attendance efficiently in the application.

---

[Back to System Overview](../../system-overview.md)