# Grades Views Documentation

## Overview

The grades feature in the student information system is implemented through three primary views:

1. **GradesView**: A general entry view for displaying the grades page.
2. **StudentGradesView**: Displays grades specific to a student, allowing them to view and filter their results.
3. **TeacherGradesView**: Allows teachers to view, add, edit, and delete grades for students enrolled in their courses.

These views support localized messages for multi-language use and provide a comprehensive interface for managing grades.

## Dependencies

- **GradeService**: Provides methods to retrieve, save, and delete grade information.
- **CourseService**: Allows teachers to select courses for grade management.
- **StudentService**: Fetches student information for displaying and editing grades.
- **UserService**: Retrieves the current logged-in student's information.
- **MessageSource**: Provides localization support.

## Features

### GradesView

- **Authenticated Access**: The `GradesView` is restricted to authenticated users (`RolesAllowed("USER")`).
- **User Content**: Displays user-specific grade content.
- **Header Component**: Reuses a common header across views.

### StudentGradesView

- **Grades Display**: Shows a list of grades specific to the logged-in student.
- **Search Functionality**: Allows students to search grades by course name.
- **Locale Support**: Texts and labels are dynamically updated based on the selected locale.
- **Grade Grid**: Displays grade information, including course name, grade value, and grading date, with a formatted layout.

### TeacherGradesView

- **Grades Management**: Teachers can add, edit, and delete grades for students.
- **Course Selection**: Teachers can select a course to manage grades for students enrolled in that course.
- **Add Grade Dialog**: Allows teachers to add new grades for students, including selecting a course and student.
- **Edit/Delete Grades**: Teachers can edit or delete grades using buttons within the grid.
- **Filter by Student**: Teachers can search for students by name to view and manage their grades.

## UI Components

- **VerticalLayout & HorizontalLayout**: Organize components visually, supporting full-width and centered layouts.
- **ComboBox** (`TeacherGradesView`): Dropdown menus for selecting courses and students.
- **TextField** (`StudentGradesView`, `TeacherGradesView`): Search fields for filtering grades and entering grade values.
- **Grid** (`StudentGradesView`, `TeacherGradesView`): Displays student grades, course details, and management actions.
- **Button Components**:
    - **Add Grade**: Allows teachers to add a new grade.
    - **Edit/Delete**: Teachers can edit or delete a grade from the list.

## Methods

- **refreshGradesData**: Fetches the latest grades based on the selected course or student number.
- **filterGrades**: Filters grades by search term in `StudentGradesView`.
- **createEditAndDeleteButtons**: Creates buttons for editing and deleting grades in the `TeacherGradesView`.
- **openAddGradeDialog** and **openEditGradeDialog** (`TeacherGradesView`): Dialogs for adding or editing a grade with input validation and user-friendly UI.
- **getMessage**: Retrieves localized messages based on the user's current locale.

## Localization

- All texts, including titles, labels, and messages, are localized using `MessageSource` and updated dynamically in response to language changes.
- Locale-based formatting for date fields ensures consistency for different languages.

## Routing

- **GradesView**: Accessible at `/grades` with a page title of "Grades".
- **StudentGradesView**: Manages the student-specific grades.
- **TeacherGradesView**: Provides teacher-specific grade management, allowing them to manage multiple courses and grades.

## Usage

- **Students**: View and search grades related to their enrolled courses.
- **Teachers**: Manage grades for students, including viewing, adding, editing, and deleting grade records.
- **Localization**: Supports multi-language usage for both students and teachers, enhancing the user experience for diverse audiences.

These views together form a comprehensive grade management module that provides functionality for both students and teachers in the system.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)