# CourseCreationRequestDTO Documentation

## Purpose

This class is a Data Transfer Object (DTO) used for transferring course creation data, including course details, associated teachers, and translations, between different layers of the application.

## Fields

- **course (Course)**: Details of the course to be created.
- **teacherIds (List<Long>)**: List of teacher IDs associated with the course.
- **translations (List<CourseTranslation>)**: List of translations for the course.

## Annotations

- **@Data**: Generates getters, setters, `toString()`, `equals()`, and `hashCode()` methods.
- **@NoArgsConstructor**: Generates a no-argument constructor.
- **@AllArgsConstructor**: Generates an all-arguments constructor.

## Usage

This DTO is used for transferring information required to create a course, typically in REST API requests.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)
