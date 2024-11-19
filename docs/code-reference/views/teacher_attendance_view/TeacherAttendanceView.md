# TeacherService Class Documentation

## Purpose

The `TeacherService` class is responsible for managing teacher-related operations, including saving, updating, retrieving, and deleting teacher data. It also handles operations related to attendance, courses, and students for teachers.

## Dependencies

- **TeacherRepository**: Used to access teacher data from the database.
- **AttendanceRepository**: Used to manage attendance records.
- **StudentRepository**: Used to access student data.
- **UserRepository**: Used to manage user-related data.

## Fields

- **teacherRepository (TeacherRepository)**: Repository for accessing teacher information.
- **attendanceRepository (AttendanceRepository)**: Repository for managing attendance data.
- **studentRepository (StudentRepository)**: Repository for accessing student data.
- **userRepository (UserRepository)**: Repository for managing user data.

## Methods

### CRUD Methods

- **save(Teacher teacher)**: Saves or updates a teacher entity in the database. If a teacher with the given username already exists, it updates the existing record. If a user with the same username already exists, it throws a `DataIntegrityViolationException`.
- **get(Long id)**: Retrieves a teacher by their ID.
- **getTeacherByUsername(String username)**: Retrieves a teacher by their username.
- **delete(Long id)**: Deletes a teacher by their ID.
- **list()**: Retrieves all teachers from the database.

### Retrieval Methods for Teacher

- **findByDepartment(String departmentName)**: Retrieves teachers by their department name.
- **findBySubject(String subjectName)**: Retrieves teachers by their subject name.
- **findByDepartmentAndSubject(String departmentName, String subjectName)**: Retrieves teachers by both department name and subject name.
- **getTeacherByUsernameWithCourses(String username)**: Retrieves a teacher by their username, including the courses they are teaching.

### Attendance Operations

- **saveAttendanceRecord(Attendance attendanceRecord)**: Saves an attendance record to the database.
- **getAttendanceRecords()**: Retrieves all attendance records from the database.
- **getAttendanceRecordsForTeacher(Long teacherId)**: Retrieves attendance records for a specific teacher based on the courses they are teaching.
- **deleteAttendanceRecord(Attendance attendanceRecord)**: Deletes an attendance record.

### Course Operations

- **getCoursesForTeacher(Long teacherId)**: Retrieves courses assigned to a specific teacher by their ID.
- **listByIds(List<Long> ids)**: Retrieves a list of teachers based on their IDs.
- **getAllTeachers()**: Retrieves all teachers from the database.

### Student Operations

- **getAllStudents()**: Retrieves all students from the database.

## Annotations

- **@Service**: Marks this class as a Spring service, allowing it to be automatically detected and managed by Spring's application context.
- **@Transactional**: Ensures that the methods execute within a transactional context, allowing for rollback in case of failure.
- **@Autowired**: Used to inject dependencies through the constructor.

## Usage

The `TeacherService` class is used to manage teacher data, including CRUD operations, attendance, and course-related actions. It serves as a bridge between the application's business logic and the underlying data repositories, ensuring that teacher-related information is consistently maintained and accessed.

