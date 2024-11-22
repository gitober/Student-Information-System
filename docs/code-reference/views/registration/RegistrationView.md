# Registration View Documentation

## Overview

The `RegistrationView` class is responsible for rendering the user registration form. It provides a user-friendly interface for new users to create an account. The registration page is accessible to unauthenticated users.

### Main Features

1. **Internationalized Form Elements**:
    - The form fields and buttons are dynamically labeled based on the current locale, using the `MessageSource` to support multiple languages.
    - The form includes fields such as first name, last name, email, password, confirm password, phone number, date of birth, and role selection.

2. **Form Layout**:
    - The registration form uses a `FormLayout` to organize fields in a user-friendly manner.
    - Includes a header with a title and uses a full-width header component (`HeaderView`) at the top.
    - The registration form layout is centered and styled for better visual appearance.

3. **Field Validation**:
    - **Email Validation**:
        - Checks for correct email format.
        - Ensures the email is not already registered.
    - **Password Validation**:
        - Validates password strength (at least 8 characters and must contain a digit).
        - Verifies that the password matches the confirmation password.
    - Real-time validation for email and passwords to improve user experience.
    - Disables the "Register" button until all fields are validated.

4. **Locale-Based Date Picker**:
    - The date picker for the birthdate is set up to use a locale-specific date format.
    - Customizes the date format specifically for Chinese locales.

5. **Form Submission**:
    - Users can select their role (either student or teacher) from a dropdown.
    - Clicking the "Register" button submits the form data, which is processed using `RegistrationHandler`.
    - Displays a notification message indicating the success or failure of registration.
    - Redirects the user to the login page upon successful registration.

6. **Responsive UI**:
    - Layouts and components are styled using CSS classes to create a visually appealing and responsive registration page.
    - The form's maximum width is set for better usability on different devices.

### Implementation Details

- **Components Used**:
    - **HeaderView**: Reusable component for consistent branding across the application.
    - **FormLayout**: For a structured form layout.
    - **DatePicker**: To collect the user's birthdate, with locale-based customization.
    - **TextField, EmailField, PasswordField, ComboBox, Button**: Collects user input and allows role selection.

- **Spring Annotations**:
    - **`@Route(value = "register")`**: Configures the path where the registration form can be accessed.
    - **`@AnonymousAllowed`**: Allows unauthenticated users to access the page.
    - **`@CssImport`**: Imports a CSS file for specific styling of the registration form.

- **Button Actions**:
    - **Register Button**: Validates all fields and submits the registration.
    - **Cancel Button**: Navigates back to the login page.

### Usage

The `RegistrationView` class is part of the public-facing interface for user registration. It ensures that new users can easily create an account, with helpful error messages guiding them through the process. Field validation and clear error handling ensure that users receive immediate feedback, which enhances their overall experience.

The page also adheres to localization standards, making it accessible to users of different languages by dynamically adjusting labels and instructions.

### Code Structure

1. **Header Setup**:
    - The `HeaderView` component provides a consistent header at the top of the page.
2. **Form Fields**:
    - Includes fields for first name, last name, birthday, email, phone number, password, and role.
    - The form is styled to align items centrally.
3. **Field Listeners**:
    - Real-time validation for email, password strength, and password matching.
4. **Form Submission Handling**:
    - Constructs a `RegistrationDTO` with all form data and passes it to `RegistrationHandler` for processing.

### Key CSS Classes for Custom Styling

- **`registration-container`**: Main container for the registration page.
- **`registration-form`**: Styling for the form layout.
- **`registration-primary-button`**: Styling for the primary "Register" button.
- **`registration-secondary-button`**: Styling for the "Cancel" button.

This documentation provides an overview of the `RegistrationView` class, highlighting its features, layout, and usage in the user registration process. Let me know if you need more details or further refinement!

---

[Back to System Overview](../../system-overview.md)