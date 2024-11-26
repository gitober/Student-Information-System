# UserContentLoader Class Documentation

## Purpose

The `UserContentLoader` class is responsible for dynamically loading user-specific content in the application's UI based on the user's role (teacher or student). It manages views for profile, courses, grades, and edit profile operations for authenticated users.

## Dependencies

- **AuthenticatedUser**: Used to get the currently authenticated user.
- **TeacherService**: Provides teacher-related operations.
- **StudentService**: Provides student-related operations.
- **DepartmentService**: Provides department-related operations for profile editing.
- **SubjectService**: Provides subject-related operations for profile editing.
- **UserService**: Provides user-related operations.
- **DateService**: Provides date formatting services.
- **MessageSource**: Used to load localized messages.
- **Various Views**: Includes different views for teachers and students, including:
    - **TeacherCoursesView**
    - **StudentCoursesView**
    - **TeacherGradesView**
    - **StudentGradesView**
    - **TeacherDashboardView**
    - **StudentDashboardView**

## Fields

- **AuthenticatedUser**: Used to retrieve information about the currently authenticated user.
- **TeacherService**: Service for managing teacher data.
- **StudentService**: Service for managing student data.
- **DepartmentService**: Service for managing department-related information.
- **SubjectService**: Service for managing subject-related information.
- **UserService**: Service for managing general user data.
- **DateService**: Service for date formatting and manipulation.
- **MessageSource**: Used for localized messages.

### Views

- **TeacherCoursesView**: View for displaying courses managed by teachers.
- **StudentCoursesView**: View for displaying courses for students.
- **TeacherGradesView**: View for displaying grades managed by teachers.
- **StudentGradesView**: View for displaying grades to students.
- **TeacherDashboardView**: View for displaying the teacher's dashboard.
- **StudentDashboardView**: View for displaying the student's dashboard.

## Methods

### Public Methods

- **`loadProfileContent(VerticalLayout layout)`**: Loads the appropriate dashboard view based on the user's role and adds it to the provided layout.
- **`loadCoursesContent(VerticalLayout layout)`**: Loads the courses view for the authenticated user based on their role and adds it to the provided layout.
- **`loadGradesContent(VerticalLayout layout)`**: Loads the grades view for the authenticated user and adds it to the provided layout.
- **`loadEditProfileContent(VerticalLayout layout)`**: Loads the appropriate profile edit view for the user, allowing them to update their details.

## Constants

- **ROLE_NOT_RECOGNIZED_MESSAGE**: Message displayed when the user's role is not recognized.
- **USER_NOT_FOUND_MESSAGE**: Message displayed when the user cannot be found (e.g., not authenticated).

## Annotations

- **`@Component`**: Marks this class as a Spring component, allowing it to be automatically detected and managed by Spring's application context.
- **`@Autowired`**: Used to inject dependencies through the constructor.
- **`@Lazy`**: Applied to view components to initialize them only when needed, helping to manage memory more efficiently.

## Usage

The `UserContentLoader` class is used to load user-specific content into the application layout, based on the authenticated user's role. It abstracts the process of identifying the user type and loading the appropriate view for profile, courses, grades, and profile editing.

---

[Back to Code Structure Overview](../../code-structure/code-structure.md)