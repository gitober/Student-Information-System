# CourseController Documentation

## Purpose

This class handles HTTP requests for managing course records, providing RESTful endpoints for CRUD operations and localization features.

## Features

- Create new courses with translations.
- Retrieve all courses or a specific course by ID.
- Update existing courses by ID, including assigning teachers.
- Delete courses by ID.
- Retrieve translated course names and plans.

## Key Methods

- **createCourse(CourseCreationRequestDTO request)**: Creates a new course along with its translations and assigned teachers.
- **getAllCourses()**: Fetches a list of all courses.
- **getCourseById(Long courseId)**: Retrieves a specific course by its ID.
- **updateCourse(Long courseId, Course course, List<Long> teacherIds)**: Updates an existing course by its ID, with associated teachers.
- **deleteCourse(Long courseId)**: Deletes a course by its ID.
- **getTranslatedCourseName(Long courseId, String locale)**: Retrieves the translated course name by course ID and locale.
- **getTranslatedCoursePlan(Long courseId, String locale)**: Retrieves the translated course plan by course ID and locale.

## Notes

### HTTP Status Codes:

- **200 OK**: Success.
- **201 Created**: New record created.
- **204 No Content**: Record deleted.
- **400 Bad Request**: Invalid teacher ID or missing parameters.
- **404 Not Found**: Course or teacher not found.

### Service Dependencies

- Utilizes **CourseService** to handle course-related business logic.
- Utilizes **TeacherService** to manage teacher assignments.

## Example Endpoints

- **Create a course**: `POST /api/courses`
- **Get all courses**: `GET /api/courses`
- **Get course by ID**: `GET /api/courses/{courseId}`
- **Update course by ID**: `PUT /api/courses/{courseId}`
- **Delete course by ID**: `DELETE /api/courses/{courseId}`
- **Get translated course name**: `GET /api/courses/{courseId}/translated-name?locale={locale}`
- **Get translated course plan**: `GET /api/courses/{courseId}/translated-plan?locale={locale}`

