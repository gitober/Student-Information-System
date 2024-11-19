# StudentController Documentation

## Purpose

This class handles HTTP requests for managing student records, providing RESTful endpoints for CRUD operations.

## Features

- Retrieve all students or a specific student by ID.
- Create new students.
- Update existing students by ID.
- Delete students by ID.

## Key Methods

- **getAllStudents()**: Retrieves a list of all students.
- **getStudentById(Long id)**: Retrieves a specific student by their ID.
- **createStudent(Student student)**: Creates a new student record.
- **updateStudent(Long id, Student student)**: Updates an existing student by their ID, modifying provided fields.
- **deleteStudent(Long id)**: Deletes a student by their ID.

## Notes

### HTTP Status Codes:

- **200 OK**: Success.
- **201 Created**: New record created.
- **204 No Content**: Record deleted.
- **404 Not Found**: Student not found.

### Service Dependency

- Utilizes **StudentService** to handle student-related business logic.

## Example Endpoints

- **Get all students**: `GET /students`
- **Get student by ID**: `GET /students/{id}`
- **Create a new student**: `POST /students`
- **Update student by ID**: `PUT /students/{id}`
- **Delete student by ID**: `DELETE /students/{id}`