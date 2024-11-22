# RegistrationDTO Documentation

## Purpose

This class is a Data Transfer Object (DTO) used for transferring user registration data, including personal details and locale information, between different layers of the application.

## Fields

- **firstName (String)**: The first name of the user.
- **lastName (String)**: The last name of the user.
- **birthday (LocalDate)**: The date of birth of the user.
- **phoneNumber (String)**: The phone number of the user.
- **email (String)**: The email address of the user.
- **password (String)**: The user's password.
- **role (String)**: The role assigned to the user.
- **currentLocale (Language)**: The current language locale for the user.

## Annotations

- **@Getter**: Generates getter methods for all fields.
- **@Setter**: Generates setter methods for all fields.
- **@Builder**: Provides a builder pattern for object creation.

## Usage

This DTO is used for transferring information required during user registration, typically in REST API requests.

---

[Back to System Overview](../../system-overview.md)

