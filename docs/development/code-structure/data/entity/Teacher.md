# Teacher Entity Documentation

## Purpose

This class represents the `Teacher` entity, which extends the `User` class, used for storing information specific to teacher users in the application.

## Fields

- **subject (Subject)**: The subject that the teacher specializes in, mapped with a many-to-one relationship.
- **department (Department)**: The department to which the teacher belongs, mapped with a many-to-one relationship.
- **courses (List<Course>)**: A list of courses taught by the teacher, mapped with a many-to-many relationship.

## Annotations

- **@Entity**: Marks this class as a JPA entity.
- **@PrimaryKeyJoinColumn(name = "user_id")**: Specifies that the `Teacher` entity shares the primary key with the `User` entity.
- **@ManyToOne(fetch = FetchType.EAGER)**: Defines a many-to-one relationship with the `Subject` and `Department` entities for the `subject` and `department` fields.
- **@JoinColumn(name = "subject_id", nullable = false)**: Specifies the foreign key column for the `subject` field.
- **@JoinColumn(name = "department_id", nullable = false)**: Specifies the foreign key column for the `department` field.
- **@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})**: Defines a many-to-many relationship with the `Course` entity for the `courses` field.
- **@JoinTable(name = "teacher_courses", joinColumns = @JoinColumn(name = "teacher_id"), inverseJoinColumns = @JoinColumn(name = "course_id"))**: Specifies the join table and columns for the `courses` field.

## Methods

- **getTeacherId()**: Returns the ID of the teacher, which is inherited from the `User` entity.
- **getFullName()**: Returns the full name of the teacher by combining their first and last names.
- **equals(Object o)**: Compares two instances of `Teacher` for equality based on their ID, subject, and department.
- **hashCode()**: Generates a hash code based on the teacher's ID, subject, and department.

## Usage

This entity is used to persist information specific to teachers, such as their subjects, department, and courses taught. It extends the `User` class to inherit common user attributes and functionality.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)
