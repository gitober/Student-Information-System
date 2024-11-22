# GradeController Documentation

## Purpose

This class handles HTTP requests for managing grade records, providing RESTful endpoints for CRUD operations.

## Features

- Retrieve grades by student number or course ID.
- Create new grades.
- Update existing grades by ID.
- Delete grades by ID.

## Key Methods

- **getGradesByStudentNumber(Long studentNumber)**: Retrieves a list of grades for a specific student using their student number.
- **getGradesByCourseId(Long courseId)**: Retrieves a list of grades for a specific course using its ID.
- **createGrade(Grade grade)**: Creates a new grade.
- **updateGrade(Integer gradeId, Grade grade)**: Updates an existing grade by its ID.
- **deleteGrade(Integer gradeId)**: Deletes a grade by its ID.

## Notes

### HTTP Status Codes:

- **200 OK**: Success.
- **201 Created**: New record created.
- **204 No Content**: Record deleted.
- **404 Not Found**: Grade not found.

### Service Dependency

- Utilizes **GradeService** to handle grade-related business logic.

## Example Endpoints

- **Get grades by student number**: `GET /api/grades/student/{studentNumber}`
- **Get grades by course ID**: `GET /api/grades/course/{courseId}`
- **Create a new grade**: `POST /api/grades`
- **Update grade by ID**: `PUT /api/grades/{gradeId}`
- **Delete grade by ID**: `DELETE /api/grades/{gradeId}`

