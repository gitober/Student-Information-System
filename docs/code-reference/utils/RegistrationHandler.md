# RegistrationHandler Class Documentation

## Purpose

The `RegistrationHandler` class is responsible for handling the user registration process, including validating the user's email, creating a new user, setting user attributes, generating student numbers, saving the user to the database, and handling translations for multilingual support.

## Dependencies

- **UserService**: Used to manage user-related operations, such as checking if an email is already registered and saving the user to the database.
- **PasswordEncoder**: Used to securely encode user passwords before saving them to the database.
- **TranslationService**: Used to save translations for user-related data to support multilingual functionality.

## Fields

- **userService (UserService)**: Provides access to user-related operations.
- **passwordEncoder (PasswordEncoder)**: Handles password encoding to ensure secure storage.
- **translationService (TranslationService)**: Manages translations for user attributes.

## Methods

### Public Methods

- **`registerUser(RegistrationDTO registrationData)`**: Handles the entire registration process for a new user. It performs the following tasks:
    - Checks if the email is already registered.
    - Creates a new user based on the selected role (Student or Teacher).
    - Sets user attributes using the provided registration data.
    - Generates a unique student number for students.
    - Saves the user to the database.
    - Adds translations for user attributes like first name, last name, and email.

  Returns `true` for successful registration and `false` if the registration fails due to invalid input or unexpected errors.

- **`isEmailRegistered(String email)`**: Checks if a given email is already registered. Returns `true` if the email is found in the database and `false` otherwise.

### Private Helper Methods

- **`createUserBasedOnRole(String role)`**: Creates a user based on the provided role, either as a `Student` or a `Teacher`. Returns `null` if the role is invalid.
- **`generateStudentNumber()`**: Generates a unique student number using the current system time in milliseconds.
- **`setUserAttributes(User user, RegistrationDTO registrationData)`**: Sets the attributes of a user, such as first name, last name, birthday, phone number, username, email, and hashed password, using the registration data.
- **`addUserTranslations(User user, RegistrationDTO registrationData)`**: Adds translations for user attributes like first name, last name, and email. These translations are stored in the database to support multilingual functionality.

## Annotations

- **`@Service`**: Marks this class as a Spring service, allowing it to be automatically detected and managed by Spring's application context.
- **`@Autowired`**: Used to inject dependencies through the constructor.

## Exception Handling

- **IllegalArgumentException**: If there is invalid input, the method logs a warning and shows a notification with the failure reason.
- **General Exception**: If any unexpected error occurs during registration, the method logs a severe message and shows a notification indicating registration failure.

## Notifications

- **`Notification.show(String message)`**: Displays notifications to inform users about the registration status, such as successful registration, email already registered, invalid role, or unexpected errors.

## Usage

The `RegistrationHandler` class is used to manage the user registration process in the application. It ensures that users are registered securely, with support for password encoding and multilingual translations for user attributes. The class also validates that each email is unique before registering a new user.
