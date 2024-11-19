# Department Entity Documentation

## Purpose

This class represents the `Department` entity, used for storing information about departments and managing relationships with teachers and subjects.

## Fields

- **departmentId (Long)**: The unique identifier for the department, generated automatically.
- **name (String)**: The name of the department.
- **teachers (Set<Teacher>)**: A set of teachers associated with the department, mapped with a one-to-many relationship.
- **subjects (Set<Subject>)**: A set of subjects associated with the department, mapped with a one-to-many relationship.

## Annotations

- **@Entity**: Marks this class as a JPA entity.
- **@Id**: Marks `departmentId` as the primary key.
- **@GeneratedValue(strategy = GenerationType.IDENTITY)**: Specifies that `departmentId` is generated automatically by the database.
- **@Column(name = "department_id")**: Maps the `departmentId` field to the `department_id` column in the table.
- **@Column(name = "department_name")**: Maps the `name` field to the `department_name` column in the table.
- **@OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)**: Defines a one-to-many relationship with the `Teacher` and `Subject` entities for the `teachers` and `subjects` fields.

## Constructors

- **Department()**: Default constructor required by JPA.
- **Department(String name)**: Constructor to create a department with a specified name.

## Methods

- **getDepartmentName()**: Returns the name of the department.
- **setDepartmentName(String name)**: Sets the name of the department.

## Usage

This entity is used to persist department information, manage teacher and subject assignments, and facilitate department-level operations within the application.

