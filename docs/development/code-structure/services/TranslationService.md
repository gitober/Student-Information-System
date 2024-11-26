# TranslationService Class Documentation

## Purpose

The `TranslationService` class is responsible for managing translations for various entities in the application, including users, courses, departments, subjects, and teachers. It provides methods to retrieve and save translations for these entities to support multilingual functionality.

## Dependencies

- **CourseTranslationRepository**: Used to access course translation data.
- **DepartmentTranslationRepository**: Used to access department translation data.
- **SubjectTranslationRepository**: Used to access subject translation data.
- **TeacherTranslationRepository**: Used to access teacher translation data.
- **UserTranslationRepository**: Used to access user translation data.

## Fields

- **courseTranslationRepository (CourseTranslationRepository)**: Repository for managing course translations.
- **departmentTranslationRepository (DepartmentTranslationRepository)**: Repository for managing department translations.
- **subjectTranslationRepository (SubjectTranslationRepository)**: Repository for managing subject translations.
- **teacherTranslationRepository (TeacherTranslationRepository)**: Repository for managing teacher translations.
- **userTranslationRepository (UserTranslationRepository)**: Repository for managing user translations.

## Methods

### User Translation Methods

- **`getUserTranslationsByLocale(Language locale)`**: Retrieves user translations for a specific locale.
- **`getUserTranslations(Long userId, Language locale)`**: Retrieves translations for a specific user and locale.
- **`saveUserTranslations(List<UserTranslation> translations)`**: Saves a list of user translations.

### Course Translation Methods

- **`getCourseTranslationsByLocale(String locale)`**: Retrieves course translations for a specific locale.
- **`getCourseTranslations(Long courseId, String locale)`**: Retrieves translations for a specific course and locale.
- **`saveCourseTranslations(List<CourseTranslation> translations)`**: Saves a list of course translations.

### Department Translation Methods

- **`getDepartmentTranslationsByLocale(String locale)`**: Retrieves department translations for a specific locale.
- **`getDepartmentTranslations(Long departmentId, String locale)`**: Retrieves translations for a specific department and locale.

### Subject Translation Methods

- **`getSubjectTranslationsByLocale(String locale)`**: Retrieves subject translations for a specific locale.
- **`getSubjectTranslations(Long subjectId, String locale)`**: Retrieves translations for a specific subject and locale.

### Teacher Translation Methods

- **`getTeacherTranslationsByLocale(String locale)`**: Retrieves teacher translations for a specific locale.
- **`getTeacherTranslations(Long teacherId, String locale)`**: Retrieves translations for a specific teacher and locale.

## Annotations

- **`@Service`**: Marks this class as a Spring service, allowing it to be automatically detected and managed by Spring's application context.
- **`@Autowired`**: Used to inject dependencies through the constructor.

## Usage

The `TranslationService` class is used to manage translation data for various entities in the application, providing multilingual support. It ensures that localized versions of data for users, courses, departments, subjects, and teachers are consistently maintained and accessed.

---

[Back to Code Structure Overview](../../code-structure/code-structure.md)