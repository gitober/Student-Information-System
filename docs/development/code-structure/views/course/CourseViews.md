# Course Management Views Documentation

## Overview

This documentation covers three key classes in the student information system related to course management:

1. **CoursesView**: Provides a general view for courses based on user roles.
2. **StudentCoursesView**: A specialized view for students, showing enrolled and available courses.
3. **TeacherCoursesView**: A specialized view for teachers, allowing them to manage their courses.

These views provide functionality such as course browsing, enrollment, attendance tracking, and course management, depending on the role of the user.

## Dependencies

- **AuthenticatedUser**: For user authentication.
- **UserContentLoader**: Loads user-specific content.
- **CourseService**: Manages course-related operations.
- **AttendanceService**: Manages attendance records for students.
- **TeacherService**: Manages teacher-related operations.
- **UserService**: Provides user-related services.
- **MessageSource**: Handles localization for displaying messages.

## Common Features

- **Dependency Injection**: Services and dependencies are injected into views using `@Autowired`.
- **Localization Support**: All views use `MessageSource` for dynamic localization, displaying messages in the user's selected language.
- **UI Layout**: The classes extend Vaadin's `Composite<VerticalLayout>` and use different components like `Grid`, `Button`, `Dialog`, etc., for creating interactive UIs.

## CoursesView

- **Purpose**: Provides a general course view, loading content specific to the authenticated user's role (student or teacher).
- **Role-Based Content**:
    - For teachers, the `TeacherCoursesView` is loaded.
    - For students, the `StudentCoursesView` is loaded.
- **Layout**:
    - Uses a `VerticalLayout` to structure the page, including a `HeaderView` and user-specific course content.

## StudentCoursesView

- **Purpose**: Allows students to view their enrolled courses, explore available courses, and manage their attendance.
- **Key Features**:
    - **Grids for Courses**: Displays enrolled and available courses using `Grid<Course>`.
    - **Search Bar**: Filters enrolled and available courses by course name or plan.
    - **Enrollment**: Provides an enrollment button for available courses.
    - **Attendance Tracking**: Shows attendance for each course using a `Dialog` for better user experience.
- **User-Specific Data**:
    - Uses `userService` to retrieve the current student's number and load courses accordingly.

## TeacherCoursesView

- **Purpose**: Enables teachers to manage their courses, view enrolled students, and perform course operations like editing, deleting, and adding new courses.
- **Key Features**:
    - **Grids for Courses**: Displays all courses managed by the teacher using `Grid<Course>`.
    - **Search Functionality**: Filters courses based on search terms.
    - **Add/Edit/Delete Courses**: Provides dialogs for adding, editing, and deleting courses.
    - **Course Details View**: Displays detailed information about a course, including a list of enrolled students.
- **Dialogs for Actions**:
    - **Edit Course**: Allows editing course details like name, plan, and teacher.
    - **Add Course**: Allows adding new courses, including assigning teachers.
    - **Delete Course**: Confirms course deletion.

## Common UI Elements

- **Grids**: Used to present tabular data for courses, attendance, and enrolled students.
- **Dialogs**: Used for user interactions like course enrollment, attendance details, and confirmations.
- **Buttons**: Various buttons are included for actions like enrollment, adding, editing, and deleting courses.
- **Styling**: Custom styles are applied using `@CssImport` to enhance the user experience.

## Localization and Notifications

- **Localization**: Messages are dynamically localized using `MessageSource` and `LocaleContextHolder` for the current locale.
- **Notifications**: Informative notifications are displayed to users during various actions like enrollment, course addition, or error handling.

## Usage

These views are part of the student information system, designed to manage courses and attendance, allowing teachers and students to interact with courses efficiently. The classes use Vaadin components to create dynamic and responsive UIs for both students and teachers, ensuring that each user's experience is tailored to their role in the system.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)