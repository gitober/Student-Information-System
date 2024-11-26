# UserService Class Documentation

## Purpose

The `UserService` class provides user-related operations, including authentication, retrieval, creation, updating, and deletion of user data. It also handles user-specific actions like password changes and retrieving the authenticated user's information.

## Dependencies

- **UserRepository**: Used to access user data from the database.
- **PasswordEncoder**: Provides password hashing for security purposes.
- **AuthenticatedUser**: Used to get the currently authenticated user.

## Fields

- **userRepository (UserRepository)**: Repository for accessing user information.
- **passwordEncoder (PasswordEncoder)**: Encoder for hashing passwords.
- **authenticatedUser (AuthenticatedUser)**: Used to manage and get the details of the authenticated user.

## Methods

### Authentication Methods

- **`authenticate(String email, String password)`**: Authenticates a user based on their email and password. Returns an `Optional<User>` if the authentication is successful.
- **`getCurrentUser()`**: Retrieves the current authenticated user.
- **`getCurrentStudentNumber()`**: Retrieves the current authenticated user's student number, if they are a student.
- **`getCurrentStudent()`**: Retrieves the current authenticated user as a `Student`, if applicable.

### CRUD Methods

- **`save(User user)`**: Saves or updates a user in the database.
- **`get(Long id)`**: Retrieves a user by their ID.
- **`delete(Long id)`**: Deletes a user by their ID.
- **`list()`**: Lists all users in the database.

### Retrieval Methods

- **`findByUsername(String username)`**: Retrieves a user by their username.
- **`findByEmail(String email)`**: Retrieves a user by their email.

### Update Methods

- **`updatePassword(String email, String newPassword)`**: Updates the user's password in the database and clears the current security context to force re-login with the new password.
- **`updateUserEmailAndUsername(Long userId, String newEmail, String newUsername)`**: Updates both email and username for a user by their user ID.

## Annotations

- **`@Service`**: Marks this class as a Spring service, allowing it to be automatically detected and managed by Spring's application context.
- **`@Autowired`**: Used to inject dependencies through the constructor.

## Logging

- **Logger**: Uses `LoggerFactory` to log important information and warnings, such as password updates and missing user details.

## Usage

The `UserService` class is used to manage user-related data, providing CRUD operations, authentication, password updates, and access to user information. It serves as the main interface for managing users within the system, ensuring user data integrity and supporting user-related operations such as updating profiles and password management.

---

[Back to Code Structure Overview](../../code-structure/code-structure.md)