# TeacherController Documentation

## Purpose

This class handles HTTP requests for managing teacher records, providing RESTful endpoints for CRUD operations.

## Features

- Retrieve all teachers or a specific teacher by ID.
- Create new teachers.
- Update existing teachers by ID.
- Delete teachers by ID.

## Key Methods

- **getAllTeachers()**: Retrieves a list of all teachers.
- **getTeacherById(Long id)**: Retrieves a specific teacher by their ID.
- **createTeacher(Teacher teacher)**: Creates a new teacher record.
- **updateTeacher(Long id, Teacher teacher)**: Updates an existing teacher by their ID, modifying provided fields.
- **deleteTeacher(Long id)**: Deletes a teacher by their ID.

## Notes

### HTTP Status Codes:

- **200 OK**: Success.
- **201 Created**: New record created.
- **204 No Content**: Record deleted.
- **404 Not Found**: Teacher not found.

### Service Dependency

- Utilizes **TeacherService** to handle teacher-related business logic.

## Example Endpoints

- **Get all teachers**: `GET /teachers`
- **Get teacher by ID**: `GET /teachers/{id}`
- **Create a new teacher**: `POST /teachers`
- **Update teacher by ID**: `PUT /teachers/{id}`
- **Delete teacher by ID**: `DELETE /teachers/{id}`

---

[Back to System Overview](../system-overview.md)


