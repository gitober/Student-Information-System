# Enrollment Entity Documentation

## Purpose

This class represents the `Enrollment` entity, used for storing information about student enrollments in courses.

## Fields

- **id (Long)**: The unique identifier for the enrollment, generated automatically.
- **studentId (Long)**: The identifier of the student associated with this enrollment.
- **courseId (Long)**: The identifier of the course associated with this enrollment.

## Annotations

- **@Entity**: Marks this class as a JPA entity.
- **@Table(name = "enrollments")**: Specifies the table name for this entity.
- **@Id**: Marks `id` as the primary key.
- **@GeneratedValue(strategy = GenerationType.IDENTITY)**: Specifies that `id` is generated automatically by the database.
- **@Column(name = "student_id")**: Maps the `studentId` field to the `student_id` column in the table.
- **@Column(name = "course_id")**: Maps the `courseId` field to the `course_id` column in the table.

## Usage

This entity is used to persist information about which students are enrolled in which courses, facilitating the management of enrollments within the application.

