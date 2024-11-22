# Course Entity Documentation

## Purpose

This class represents the `Course` entity, used for storing course information and managing relationships with teachers and students.

## Fields

- **courseId (Long)**: The unique identifier for the course, generated automatically.
- **courseName (String)**: The name of the course.
- **coursePlan (String)**: The detailed plan for the course.
- **duration (Integer)**: The duration of the course in days.
- **teachers (List<Teacher>)**: A list of teachers associated with the course, mapped with a many-to-many relationship.
- **students (Collection<Student>)**: A collection of students enrolled in the course, mapped with a many-to-many relationship.

## Annotations

- **@Entity**: Marks this class as a JPA entity.
- **@Table(name = "course")**: Specifies the table name for this entity.
- **@Id**: Marks `courseId` as the primary key.
- **@GeneratedValue(strategy = GenerationType.IDENTITY)**: Specifies that `courseId` is generated automatically by the database.
- **@Column(name = "course_name", nullable = false)**: Maps the `courseName` field to the `course_name` column in the table.
- **@Column(name = "course_plan", nullable = false)**: Maps the `coursePlan` field to the `course_plan` column in the table.
- **@Column(name = "duration", nullable = false)**: Maps the `duration` field to the `duration` column in the table.
- **@ManyToMany(fetch = FetchType.EAGER)**: Defines a many-to-many relationship with the `Teacher` entity for the `teachers` field.
- **@JoinTable(name = "teacher_courses", joinColumns = @JoinColumn(name = "course_id"), inverseJoinColumns = @JoinColumn(name = "teacher_id"))**: Specifies the join table and columns for the `teachers` field.
- **@ManyToMany(mappedBy = "courses")**: Defines a many-to-many relationship with the `Student` entity for the `students` field.

## Constructors

- **Course()**: Default constructor required by JPA.
- **Course(String courseName, String coursePlan, Integer duration)**: Constructor to create a course with specified values (excluding `courseId`).

## Additional Methods

- **getFormattedDateRange()**: Returns the formatted date range for the course, calculated using the duration and the current date, formatted based on the user's locale.
- **getStudents()**: Returns the collection of students enrolled in the course.
- **setStudents(Collection<Student> students)**: Sets the collection of students enrolled in the course.

## Usage

This entity is used to persist course information, manage teacher assignments, and track student enrollments, typically used in course management features.

---

[Back to System Overview](../../system-overview.md)
