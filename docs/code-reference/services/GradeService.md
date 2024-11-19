# GradeService Class Documentation

## Purpose

The `GradeService` class is responsible for managing grade-related operations, including saving, updating, retrieving, and deleting grade data.

## Dependencies

- **GradeRepository**: Used to access grade data from the database.

## Fields

- **gradeRepository (GradeRepository)**: Repository for accessing grade information.

## Methods

### CRUD Methods

- **getAllGrades()**: Retrieves all grades from the database.
- **saveGrade(Grade grade)**: Saves or creates a new grade in the database.
- **updateGrade(Integer gradeId, Grade grade)**: Updates an existing grade by its ID.
    - Returns `null` if the grade does not exist.
- **deleteGrade(Integer gradeId)**: Deletes a grade by its ID.
    - Returns `true` if the deletion was successful, `false` if the grade was not found.

### Retrieval Methods

- **getGradesByStudentNumber(Long studentNumber)**: Retrieves grades by the student's number.
- **getGradesByCourseId(Long courseId)**: Retrieves grades by the course ID.

## Annotations

- **@Service**: Marks this class as a Spring service, allowing it to be automatically detected and managed by Spring's application context.
- **@Autowired**: Used to inject dependencies through the constructor.

## Usage

The `GradeService` class is used to manage grade data, including CRUD operations and retrieval based on student or course details. It serves as the intermediary between the application logic and the database, ensuring that grade data is consistently managed.
