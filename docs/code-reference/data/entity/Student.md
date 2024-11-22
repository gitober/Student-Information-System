# Student Entity Documentation

## Purpose

This class represents the `Student` entity, which extends the `User` class, used for storing information specific to student users in the application.

## Fields

- **address (String)**: The address of the student.
- **grade (String)**: The grade level of the student.
- **studentClass (String)**: The class of the student.
- **courses (Set<Course>)**: A set of courses associated with the student, mapped with a many-to-many relationship.

## Annotations

- **@Entity**: Marks this class as a JPA entity.
- **@DiscriminatorValue("STUDENT")**: Specifies the discriminator value for the `Student` entity when using single table inheritance.
- **@ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})**: Defines a many-to-many relationship with the `Course` entity for the `courses` field.
- **@JoinTable(name = "registration", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"), inverseJoinColumns = @JoinColumn(name = "course_id", referencedColumnName = "course_id"))**: Specifies the join table and columns for the `courses` field.

## Methods

- **equals(Object o)**: Compares two instances of `Student` for equality based on their ID, address, grade, and studentClass.
- **hashCode()**: Generates a hash code based on the student's ID, address, grade, and studentClass.

## Usage

This entity is used to persist information specific to students, such as their address, grade, and enrolled courses. It provides a many-to-many relationship to manage the courses that students are enrolled in.

