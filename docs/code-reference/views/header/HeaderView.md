# HeaderView Documentation

## Overview

The `HeaderView` class provides a flexible header component for the student information system. It serves both logged-in users and public visitors with distinct variations in the displayed elements, such as navigation links and language options.

## Dependencies

- **AuthenticatedUser**: Provides user details for authenticated users to customize the header.
- **MessageSource**: Handles localization for dynamic translations based on the user's selected language.
- **Vaadin Components**: Utilizes various Vaadin components like `Button`, `Image`, `Span`, and layouts to build the header.

## Features

### Two Header Variants

1. **Full Header for Logged-In Users**: Displays a navigation bar with links to the application's main features (e.g., home, courses, grades, attendance tracking, profile). Includes language selection flags and a logout button.
2. **Minimal Header for Public Pages**: Displays only the logo, app title, and language flags.

### Dynamic Navigation Links

- Depending on the authenticated user's role, different links are displayed:
    - **Home** (`ProfilePageView`): Takes the user to their profile page.
    - **Courses** (`CoursesView`): Directs the user to the courses page.
    - **Grades** (`GradesView`): Shows the user's grades.
    - **Attendance Tracking** (`TeacherAttendanceView`): Available for teachers to manage attendance.
    - **Update Student Profiles** (`TeacherUpdateStudentProfileView`): Available for teachers to manage student profiles.
    - **Edit Profile** (`EditProfileView`): Allows users to edit their profile.

### Language Flags

- **Language Selection**: Users can select their preferred language by clicking on flag icons.
    - Supported languages: **English**, **Finnish**, **Chinese**, **Russian**.
    - Flags are clickable, allowing users to change the application language.
    - **Logout Button Customization**: Changes the logout button's color when Chinese is selected to maintain consistency with language-specific styles.

### Logout Functionality

- The logout button is present in the full header for logged-in users.
- When clicked, it navigates the user to the `/logout` route.

### Localization

- All texts, such as button labels, navigation links, and tooltips, are dynamically localized.
- **MessageSource** and `LocaleContextHolder` are used to fetch and apply translations based on the user's locale.

## UI Components

- **Logo and Title**: Displays the logo and the application name. It is always present in both full and minimal headers.
- **RouterLink**: Provides navigation links for logged-in users.
- **Language Flags Layout**: Allows users to switch between supported languages using flag images.
- **Logout Button**: Facilitates secure logout for authenticated users.

## Methods

- **createLanguageFlags()**: Creates a `HorizontalLayout` containing flag images for each supported language.
- **createFlagImage(imagePath, locale)**: Creates an image element representing a flag and adds a click listener to change the application's locale.
- **getTranslation(key)**: Retrieves localized messages using the provided key and the current locale.

## Styling

- **CSS Classes**: The header, language flags, logo, logout button, and other elements have corresponding CSS classes that are imported for consistent styling.
- **Header Styling**: The header has a full-width layout, is fixed at `60px` in height, and centers the elements vertically. Additional styles are applied dynamically based on the selected language.

## Usage

- **Full Header**: Used when a user is authenticated and logged in to the system, providing them with all navigation options.
- **Minimal Header**: Used for public pages like login and registration, allowing language selection without any user-specific navigation.

The `HeaderView` thus acts as a unified navigation and settings point for both logged-in and guest users, supporting multiple languages and providing a consistent and intuitive user interface.
