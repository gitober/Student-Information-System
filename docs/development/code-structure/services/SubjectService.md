# SubjectService Class Documentation

## Purpose

The `SubjectService` class is responsible for managing subject-related operations, including retrieving all subjects and ensuring a default subject is available.

## Dependencies

- **SubjectRepository**: Used to access subject data from the database.

## Fields

- **subjectRepository (SubjectRepository)**: Repository for accessing subject information.

## Methods

### CRUD Methods

- **findAll()**: Retrieves all subjects from the database.

### Additional Methods

- **findDefaultSubject()**: Finds a default subject, creating one if none exists. If a subject named "Default Subject" is not found, a new one is created and saved to the database.

## Annotations

- **@Service**: Marks this class as a Spring service, allowing it to be automatically detected and managed by Spring's application context.
- **@Autowired**: Used to inject dependencies through the constructor.

## Usage

The `SubjectService` class is used to manage subject data, including retrieval of all subjects and ensuring a default subject exists for operations. It acts as an essential part of maintaining subject information in the application.

---

[Back to Code Structure Overview](../../code-structure/code-structure.md)
