# Login Page View Documentation

## Overview

The `LoginPageView` is a user-friendly login interface for the application. It allows users to sign in, sign up, and request a password reset if needed. The page includes localization for multilingual support and features a welcoming and straightforward layout.

### Main Features

1. **Localization and Internationalization**
    - All text elements such as labels, buttons, and notifications are localized using `MessageSource` to support multiple languages.
    - The page can dynamically switch languages as per user preference.

2. **Components**
    - **EmailField**: For user email input.
    - **PasswordField**: For user password input.
    - **Remember Me Checkbox**: Allows users to save their email in a cookie for future logins.
    - **Sign In Button**: Triggers login logic.
    - **Sign Up Button**: Redirects users to the registration page.
    - **Forgot Password Button**: Navigates to the forgot password view for password reset.

3. **HeaderView Integration**
    - The login page includes a header that provides navigation and language selection options for unauthenticated users.
    - `HeaderView` is reused for consistency across pages, adding a common brand logo and language flags.

### Layout Details

- **Main Layout**:
    - Uses a `VerticalLayout` to provide a full-page login experience.
    - Aligns all components to the center for uniformity.
    - Adds a spacer to ensure that the content starts below the header.

- **Content Layout**:
    - **Left Content**: Contains the login form, including email, password fields, "Remember Me" checkbox, and buttons for signing in, signing up, and password reset.
        - Text labels provide a welcoming message, instructions, and prompts for users without an account.
    - **Right Content**: Displays a decorative bird image, enhancing the visual appeal and providing brand recognition.

### Functionality

- **Login Process**:
    - Users enter their email and password, and click the "Sign In" button.
    - If fields are empty, notifications are shown indicating the error.
    - The `LoginHandler` is invoked to validate the credentials. Success or failure messages are shown accordingly.

- **"Remember Me" Functionality**:
    - If the user selects the "Remember Me" option, the email is stored in a browser cookie.
    - When users return, their email is prefilled if the cookie exists.

- **User Navigation**:
    - **Sign Up**: The "Sign Up" button takes users to the registration page.
    - **Forgot Password**: Users can navigate to the "Forgot Password" page to initiate a password reset process.

### Visual Customization

- **CSS Classes**:
    - Unique CSS classes are used for every component for easy styling.
    - CSS classes are assigned to all the primary elements including buttons, text fields, checkboxes, and layouts, providing clear, consistent styling for the entire login page.

### Code Structure

- **Login Form Elements Setup**:
    - Form elements are initialized with localized texts.
    - The layout is separated into left and right sections for better visual structure.

- **Cookie Handling**:
    - The "Remember Me" cookie, if set, is loaded when the page is initialized to prefill the email field.

## Usage

This `LoginPageView` ensures that users have an intuitive and welcoming entry point to the application, featuring user-friendly options like "Remember Me", and localization support for a multilingual audience.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)