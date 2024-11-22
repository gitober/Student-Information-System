# Subject Entity Documentation

## Purpose

This class represents the `Subject` entity, used for storing information about subjects in the application.

## Fields

- **id (Long)**: The unique identifier for the subject, generated automatically.
- **name (String)**: The name of the subject.
- **department (Department)**: The department to which the subject belongs, mapped with a many-to-one relationship.

## Annotations

- **@Entity**: Marks this class as a JPA entity.
- **@Id**: Marks `id` as the primary key.
- **@GeneratedValue(strategy = GenerationType.IDENTITY)**: Specifies that `id` is generated automatically by the database.
- **@Column(name = "subject_id")**: Maps the `id` field to the `subject_id` column in the table.
- **@ManyToOne(fetch = FetchType.LAZY)**: Defines a many-to-one relationship with the `Department` entity for the `department` field.
- **@JoinColumn(name = "department_id", nullable = false)**: Specifies the foreign key column for the `department` field.

## Constructors

- **Subject()**: Default constructor required by JPA.
- **Subject(String name, Department department)**: Constructor to create a subject with a specified name and department.

## Usage

This entity is used to persist information about subjects, including their names and associated departments. It is typically used in the management of academic departments and their offerings.

