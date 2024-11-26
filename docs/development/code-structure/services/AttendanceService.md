# AttendanceService Class Documentation

## Purpose

The `AttendanceService` class is responsible for managing attendance records, including retrieving, creating, updating, and deleting attendance data. It interacts with the `AttendanceRepository` to perform CRUD operations.

## Dependencies

- **AttendanceRepository**: Used to access attendance data from the database.

## Fields

- **attendanceRepository (AttendanceRepository)**: Repository for accessing attendance information.

## Methods

### CRUD Methods

- **getAllAttendance()**: Retrieves all attendance records from the database.
- **getAttendanceById(Long attendanceId)**: Retrieves an attendance record by its ID.
- **createAttendance(Attendance attendance)**: Creates a new attendance record in the database.
- **updateAttendance(Long attendanceId, Attendance attendance)**: Updates an existing attendance record by its ID.
    - Throws `NoSuchElementException` if the attendance record is not found.
- **deleteAttendance(Long attendanceId)**: Deletes an attendance record by its ID.
    - Returns `true` if the deletion was successful, `false` if the record was not found.

### Additional Methods

- **getAttendanceByCourseId(Long courseId)**: Retrieves attendance records by course ID.
- **getAttendanceByStudentNumberAndCourseId(Long studentNumber, Long courseId)**: Retrieves attendance records by student number and course ID.

## Annotations

- **@Service**: Marks this class as a Spring service, allowing it to be automatically detected and managed by Spring's application context.
- **@Autowired**: Used to inject the `AttendanceRepository` dependency through the constructor.

## Usage

The `AttendanceService` class is used to manage attendance records, providing methods for basic CRUD operations as well as specific methods for retrieving attendance data by course or student. It serves as the intermediary between the application logic and the database, ensuring that attendance data is consistently managed.

---

[Back to Code Structure Overview](../../code-structure/code-structure.md)
