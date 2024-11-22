# Edit Profile Views Documentation

## Overview

This documentation covers the three main classes used for editing user profiles in the student information system:

1. **EditProfileView**: Provides a general page for accessing profile editing for users.
2. **StudentEditProfileView**: A view tailored for students to update their personal information.
3. **TeacherEditProfileView**: A view for teachers to update their personal information, including department and subject details.

These views allow users to manage their profile information with role-specific features and layouts.

## Dependencies

- **AuthenticatedUser**: For user authentication and ensuring the correct profile is being edited.
- **UserContentLoader**: Loads the user-specific content for editing profiles.
- **UserService**: Provides user-related operations, including saving updated profiles.
- **DepartmentService**: Retrieves department details for teachers.
- **SubjectService**: Retrieves subject details for teachers.
- **MessageSource**: Handles localization for displaying messages.

## Common Features

- **Dependency Injection**: Dependencies are injected using `@Autowired`.
- **Localization Support**: All views use `MessageSource` for dynamic localization.
- **UI Layout**: Views use `VerticalLayout` and other Vaadin components such as `TextField`, `Button`, `ComboBox`, and `Notification` for creating an intuitive user interface.

## EditProfileView

- **Purpose**: Provides a general edit profile page, loading specific profile editing components based on the user role (student or teacher).
- **Role-Based Content**:
    - Uses `UserContentLoader` to dynamically load either `StudentEditProfileView` or `TeacherEditProfileView` depending on the authenticated user's role.
- **Layout**:
    - Uses `VerticalLayout` to structure the page, including a reusable header (`HeaderView`) and user-specific profile content.

## StudentEditProfileView

- **Purpose**: Allows students to update their personal information, such as their name, email, and phone number.
- **Key Features**:
    - **Current Details**: Displays current profile details, including full name, email, and phone number.
    - **Update Form**: Provides a form to update information including first name, last name, email, phone number, and password.
    - **Save Button**: Saves updated profile details and displays a success notification.
    - **Layout**:
        - **Current Details Section**: Uses `Paragraph` components to display current profile details.
        - **Form Section**: Allows students to update specific fields.

## TeacherEditProfileView

- **Purpose**: Allows teachers to update their personal information, including their department and subject.
- **Key Features**:
    - **Current Details**: Displays the current profile details of the teacher, including full name, email, phone number, department, and subject.
    - **Update Form**: Allows teachers to update their personal details, department, and subject.
    - **Save Button**: Saves updated profile details and displays a success notification.
    - **Layout**:
        - **Current Details Section**: Similar to `StudentEditProfileView`, but with additional fields for department and subject.
        - **Form Section**: Allows teachers to update specific fields like name, department, and subject.

## Common UI Elements

- **Form Fields**: Both student and teacher forms use `TextField` components to capture user input. Teachers also use `ComboBox` components for selecting a department and subject.
- **Save Button**: When clicked, updates the respective user's profile information.
- **Notification**: Displays success messages after successfully updating the profile.

## Usage

These views are designed to manage user profile updates for both students and teachers. Depending on the user's role, the corresponding profile view (`StudentEditProfileView` or `TeacherEditProfileView`) is loaded with appropriate options and fields to ensure that the right information is editable. This enhances the user experience by ensuring that profile updates are intuitive and role-specific.

---

[Back to System Overview](../../system-overview.md)