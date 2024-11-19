# AuthenticatedUser Class Documentation

## Purpose

The `AuthenticatedUser` class handles authentication and user details retrieval for the currently logged-in user. It provides methods to fetch the authenticated user entity and to log out the user.

## Dependencies

- **UserRepository**: Used to retrieve user information from the database based on the authenticated user's details.
- **AuthenticationContext**: Provided by Vaadin, used to access the current authentication context and manage logout operations.

## Fields

- **userRepository (UserRepository)**: Repository for accessing user data.
- **authenticationContext (AuthenticationContext)**: Handles the authentication context, including fetching the currently authenticated user and logging out.
- **logger (Logger)**: Logger instance for logging messages related to authentication.

## Methods

- **get()**: Fetches the authenticated user's details as an `Optional<User>`.
    - Retrieves the `UserDetails` of the currently authenticated user using `authenticationContext`.
    - Logs the authentication status (either authenticated user details or no user found).
    - Maps the `UserDetails` to the custom `User` entity by fetching it from the `UserRepository` using the user's email.

- **logout()**: Logs out the currently authenticated user.
    - Uses `authenticationContext.logout()` to log out the user.
    - Logs the successful logout event.

## Annotations

- **@Component**: Marks this class as a Spring component, allowing it to be automatically detected and managed by Spring's application context.
- **@Transactional**: Ensures that the `get()` method runs within a transactional context, useful for managing database operations consistently.

## Usage

The `AuthenticatedUser` class is used to manage user authentication operations, such as retrieving the authenticated user's entity or logging out. This is useful for maintaining session-specific user data and handling security within the application.

