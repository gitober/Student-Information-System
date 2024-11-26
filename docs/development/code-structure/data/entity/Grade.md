# Grade Entity Documentation

## Purpose

This class represents the `Grade` entity, used for storing information about student grades in courses.

## Fields

- **gradeId (Integer)**: The unique identifier for the grade, generated automatically.
- **gradeValue (String)**: The value of the grade, such as 'A', 'B+', etc., with a maximum length of 5 characters.
- **gradingDay (LocalDate)**: The date when the grade was assigned.
- **studentNumber (Long)**: The unique number of the student associated with this grade.
- **course (Course)**: The course associated with this grade, mapped with a many-to-one relationship.

## Annotations

- **@Entity**: Marks this class as a JPA entity.
- **@Table(name = "grade")**: Specifies the table name for this entity.
- **@Id**: Marks `gradeId` as the primary key.
- **@GeneratedValue(strategy = GenerationType.IDENTITY)**: Specifies that `gradeId` is generated automatically by the database.
- **@Column(name = "grade", nullable = false, length = 5)**: Maps the `gradeValue` field to the `grade` column in the table.
- **@Column(name = "grading_day", nullable = false)**: Maps the `gradingDay` field to the `grading_day` column in the table.
- **@Column(name = "student_number", nullable = false)**: Maps the `studentNumber` field to the `student_number` column in the table.
- **@ManyToOne**: Defines a many-to-one relationship with the `Course` entity for the `course` field.
- **@JoinColumn(name = "course_id", referencedColumnName = "course_id", nullable = false)**: Specifies the foreign key column for the `course` field.

## Usage

This entity is used to persist information about student grades in specific courses, allowing the application to manage grading and reporting functionalities.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)
