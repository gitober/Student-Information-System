# ForgotPasswordView Documentation

## Overview

The `ForgotPasswordView` class provides a user interface for handling forgotten password and password reset processes in the student information system. It consists of two main sections:

1. **Forgot Password Section**: Allows users to enter their email address to receive a password reset link.
2. **Reset Password Section**: Allows users to enter a new password using a provided reset token.

This view helps users recover their accounts and reset their passwords securely.

## Dependencies

- **EmailService**: Handles email-related operations such as sending the reset password link and updating passwords.
- **MessageSource**: Provides support for localization of messages.
- **MainLayout**: Sets the common layout for the application pages.

## Features

### Forgot Password Section

- **Email Input**: Users provide their email address to request a password reset link.
- **Submit Button**: Sends a reset token to the email address provided.
- **Cancel Button**: Navigates back to the login page.

### Reset Password Section

- **Password Input**: Users set a new password after clicking the reset link.
- **Reset Button**: Saves the new password.
- **Cancel Button**: Navigates back to the login page.

### Localization

- All UI texts, including labels, instructions, and button labels, are updated dynamically based on the user's locale.
- **LocaleChangeObserver** is implemented to update UI components when the application's language changes.

### Token Validation

- When users click the link sent to their email, a token is validated to ensure that they are authorized to reset their password.
- If the token is valid, the reset password layout becomes visible for the user to set a new password.

## UI Components

- **EmailField** (`forgotPasswordLayout`): Captures the user's email for requesting a reset link.
- **PasswordField** (`resetPasswordLayout`): Captures the new password during the reset process.
- **Button Components**:
    - **Submit**: Sends a reset token via email.
    - **Cancel**: Returns to the login page.
    - **Reset**: Submits the new password.
    - **Reset Cancel**: Returns to the login page without changing the password.
- **Image**: Displays a bird image for visual enhancement.

## Methods

- **handleForgotPassword**: Generates a reset token, saves it, and sends an email to the user.
- **validateResetToken**: Validates the provided token and toggles the view to the reset password section if valid.
- **handleResetPassword**: Saves the new password after token validation.
- **updateTextsBasedOnLocale**: Updates UI component texts based on the selected locale.
- **initializeUI**: Initializes the main layout, adding both the forgot password and reset password sections.

## Routing

- **PageTitle**: "Forgot Password / Reset Password"
- **Route**: Accessible via `/forgotpassword`, with `MainLayout` as the parent layout.
- **AnonymousAllowed**: Allows unauthenticated users to access this view.

## Usage

The `ForgotPasswordView` is used to assist users who need to recover access to their accounts by resetting their passwords. It allows users to securely request a reset link, set a new password, and update their credentials.

---

[Back to Code Structure Overview](../../../code-structure/code-structure.md)