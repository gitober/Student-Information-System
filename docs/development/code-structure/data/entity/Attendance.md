# Attendance Entity Documentation

## Purpose

This class represents the `Attendance` entity, used for storing attendance records for students in courses.

## Fields

- **attendanceId (Long)**: The unique identifier for the attendance record, generated automatically.
- **attendanceStatus (String)**: The status of attendance (e.g., 'Present', 'Absent').
- **attendanceDate (LocalDate)**: The date of the attendance.
- **student (Student)**: The student associated with the attendance record, mapped with a many-to-one relationship.
- **course (Course)**: The course associated with the attendance record, mapped with a many-to-one relationship.

## Annotations

- **@Entity**: Marks this class as a JPA entity.
- **@Table(name = "attendance")**: Specifies the table name for this entity.
- **@Id**: Marks `attendanceId` as the primary key.
- **@GeneratedValue(strategy = GenerationType.IDENTITY)**: Specifies that `attendanceId` is generated automatically by the database.
- **@Column(name = "attendance_status", nullable = false)**: Maps the `attendanceStatus` field to the `attendance_status` column in the table.
- **@Column(name = "attendance_date", nullable = false)**: Maps the `attendanceDate` field to the `attendance_date` column in the table.
- **@ManyToOne(fetch = FetchType.LAZY)**: Defines a many-to-one relationship with the `Student` and `Course` entities.
- **@JoinColumn(name = "student_number", referencedColumnName = "student_number")**: Specifies the foreign key column for the `student` field.
- **@JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)**: Specifies the foreign key column for the `course` field.

## Constructors

- **Attendance()**: Default constructor required by JPA.
- **Attendance(String attendanceStatus, LocalDate attendanceDate, Student student, Course course)**: Constructor to create an attendance record with specified values (excluding `attendanceId`).

## Usage

This entity is used to persist attendance information for students in specific courses, typically used in attendance tracking features.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)
