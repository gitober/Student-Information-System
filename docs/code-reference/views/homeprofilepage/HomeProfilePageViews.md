# Profile Page and Dashboard Views Documentation

## Overview

These views are used to present both students and teachers with an overview of relevant information, providing interactive navigation options like editing profiles, viewing courses, attendance, and grades.

### Classes Overview

- **ProfilePageView**: The main profile page for users, presenting basic profile information and navigation links.
- **StudentDashboardView**: A dashboard tailored for students to access their courses, grades, attendance, and profile settings.
- **TeacherDashboardView**: A dashboard designed for teachers, allowing them to manage student profiles, view and assign grades, track attendance, and access course details.

## Common Features

### Layout and Styling

- All views use a consistent **VerticalLayout** with CSS classes for styling.
- **HeaderView** is added to the profile page for navigation and language settings.
- **Dashboard Grid Container**: Uses `FlexLayout` to create a responsive grid that displays cards containing information or navigation links.

### Dynamic Content

- **Localized Texts**: All labels and descriptions are localized using `MessageSource` to support multiple languages.
- **Navigation Cards**: Cards in the dashboard link to key features such as courses, grades, and attendance. Each card displays a title, description, and icon.

### Reusable Components

- **Dashboard Cards**: Created with a consistent layout, showing an icon, title, and description. Depending on the context, cards may either link to another view or trigger actions (e.g., opening a dialog).
- **Attendance Dialog**: The student view includes a dialog to show attendance details for enrolled courses.

## Specific Features

### ProfilePageView

- **Purpose**: Displays user profile information with navigation links for easy access to other parts of the application.
- **Components**:
    - **HeaderView**: Provides navigation, including language options.
    - **User-Specific Content**: Uses `UserContentLoader` to load profile-specific content dynamically.

### StudentDashboardView

- **Purpose**: Provides students with an overview of their courses, grades, attendance, and options to edit their profile.
- **Components**:
    - **Welcome Message**: Personalized greeting for the student.
    - **Dashboard Cards**: Navigation to courses, grades, attendance, and profile editing. Also includes an attendance dialog for more detailed viewing.

### TeacherDashboardView

- **Purpose**: Offers teachers a centralized location to manage their students, grades, courses, and attendance.
- **Components**:
    - **Welcome Message**: Personalized greeting for the teacher.
    - **Dashboard Cards**: Navigation options to manage student profiles, assign grades, view courses, track attendance, and access quick links for editing their profile.

## Usage

- **ProfilePageView** is displayed when a user navigates to the "profile" route. It contains general profile details and links to key actions.
- **StudentDashboardView** and **TeacherDashboardView** provide more specialized information depending on the user role. The teacher view is more focused on management, while the student view offers a summary of their academic details.

These views ensure a consistent user experience across both student and teacher roles, with navigation and actions tailored to their specific needs.
