# EmailService Class Documentation

## Purpose

The `EmailService` class is responsible for sending emails, generating and managing reset tokens, and updating user passwords. It serves as the component for managing email-related operations, including password reset functionality.

## Dependencies

- **JavaMailSender**: Used to send emails.
- **BCryptPasswordEncoder**: Used to hash passwords before saving them in the database.
- **UserRepository**: Used to manage user data and update passwords.

## Fields

- **resetTokenStore (Map<String, String>)**: In-memory storage for reset tokens.
- **mailSender (JavaMailSender)**: Responsible for sending email messages.
- **passwordEncoder (BCryptPasswordEncoder)**: Used to encrypt passwords before saving them to the database.
- **userRepository (UserRepository)**: Repository for accessing and updating user data.
- **fromEmail (String)**: Sender's email address, injected from application properties.

## Methods

### Public Methods

- **sendEmail(String to, String subject, String text)**: Sends an email to the specified recipient with the provided subject and text content.
- **generateResetToken()**: Generates a unique reset token using `UUID`.
- **saveResetTokenToMemory(String email, String token)**: Saves a reset token in the in-memory storage for the specified email.
- **getEmailByToken(String token)**: Retrieves the email address associated with the given reset token.
- **updatePassword(String email, String newPassword)**: Updates the user's password in the database after hashing it.

## Annotations

- **@Service**: Marks this class as a Spring service, allowing it to be automatically detected and managed by Spring's application context.
- **@Autowired**: Used to inject dependencies through the constructor.
- **@Value**: Used to inject the sender's email address from application properties.

## Usage

The `EmailService` class is used for managing email operations such as sending emails, handling password reset requests, and updating user passwords. It provides utility methods to generate and manage reset tokens, ensuring secure password reset functionality for users.

---

[Back to Code Structure Overview](../../code-structure/code-structure.md)
