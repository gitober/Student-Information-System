# CourseService Class Documentation

## Purpose

The `CourseService` class is responsible for managing course-related operations, including saving, updating, retrieving, and deleting course data. It also handles course enrollment, registration, and translation of course fields.

## Dependencies

- **CourseRepository**: Used to access course data from the database.
- **RegistrationRepository**: Manages course enrollment and registration information.
- **TeacherRepository**: Used to manage and retrieve teacher data.
- **StudentRepository**: Used to manage and retrieve student data.
- **TranslationService**: Used to handle translations of course-related fields.

## Fields

- **courseRepository (CourseRepository)**: Repository for accessing course information.
- **registrationRepository (RegistrationRepository)**: Repository for managing course registrations.
- **teacherRepository (TeacherRepository)**: Repository for accessing teacher information.
- **studentRepository (StudentRepository)**: Repository for accessing student information.
- **translationService (TranslationService)**: Service for managing course translations.

## Methods

### CRUD Methods

- **saveCourse(Course course, List<Teacher> teachers, List<CourseTranslation> translations)**: Saves a new course along with its associated teachers and translations.
- **updateCourse(Long courseId, Course course, List<Teacher> teachers)**: Updates an existing course by its ID, including updating its associated teachers.
- **getAllCourses()**: Retrieves all courses from the database.
- **getCourseById(Long courseId)**: Retrieves a course by its ID.
- **deleteCourse(Long courseId)**: Deletes a course by its ID.
    - Returns `true` if the deletion was successful, `false` if the course was not found.

### Enrollment and Registration Methods

- **enrollInCourse(Long studentNumber, Long batchId, Long courseId, double coursePayment)**: Enrolls a student in a course, creating a registration record.
- **getEnrolledCourses(Long studentNumber)**: Retrieves courses in which a student is enrolled by their student number.
- **getEnrolledStudents(Long courseId)**: Retrieves students enrolled in a course by its course ID.

### Additional Methods

- **getAvailableCourses()**: Retrieves all available courses.
- **getCoursesForStudent(Long studentId)**: Retrieves courses for a specific student by their student ID.
- **getEnrolledStudentsByCourseId(Long courseId)**: Retrieves students enrolled in a course by its course ID.
- **getCoursesByTeacherId(Long teacherId)**: Retrieves courses taught by a specific teacher by their teacher ID.
- **getTranslatedName(Long courseId, String languageCode)**: Retrieves the translated name of a course.
- **getTranslatedPlan(Long courseId, String languageCode)**: Retrieves the translated plan of a course.

### Helper Methods

- **getTranslation(Long courseId, String fieldName, String languageCode)**: Helper method to fetch the translation of a course field based on the field name and language code.

## Annotations

- **@Service**: Marks this class as a Spring service, allowing it to be automatically detected and managed by Spring's application context.
- **@Autowired**: Used to inject dependencies through the constructor.
- **@Transactional**: Ensures that the `saveCourse` method runs within a transactional context, useful for managing database operations consistently.

## Usage

The `CourseService` class is used to manage course data, including CRUD operations, enrollment, registration, and translation. It serves as the intermediary between the application logic and the database, ensuring that course data is consistently managed and that relationships between courses, teachers, and students are maintained.

---

[Back to Code Structure Overview](../../code-structure/code-structure.md)
