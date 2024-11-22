# LoginHandler Class Documentation

## Purpose

The `LoginHandler` class is responsible for managing user login operations, including authentication, handling "remember me" functionality, setting security context, and redirecting users based on their roles.

## Dependencies

- **UserService**: Used for user-related operations, including authentication.
- **AuthenticationManager**: Handles the authentication process for Spring Security.

## Fields

- **userService (UserService)**: Provides access to user-related operations.
- **authenticationManager (AuthenticationManager)**: Responsible for authenticating users.
- **PROFILE_ROUTE (String)**: The route to navigate to after successful login.

## Methods

### Login Method

- **`login(String email, String password, boolean rememberMe)`**: Handles the login process for a user. Authenticates the user using their email and password, and optionally enables "remember me" functionality. Returns `true` for successful login and `false` for failed login.

### Private Helper Methods

- **`handleSuccessfulLogin(User user, String email, String password, boolean rememberMe)`**: Handles the actions to be taken upon successful login, including setting the security context, managing cookies for "remember me", and navigating based on the user's role.
- **`handleFailedLogin(String email)`**: Handles actions to be taken when login fails, including logging a warning message and showing a notification.
- **`handleRememberMeCookie(boolean rememberMe, String email, HttpServletResponse response)`**: Sets or removes the "remember me" cookie based on the user's choice. The cookie is set for 30 days if "remember me" is selected.
- **`navigateUserBasedOnRole(User user)`**: Navigates the user to the appropriate route based on their role (e.g., teacher or student).
- **`showNotification(String message)`**: Displays a notification message within the UI context.

## Annotations

- **`@Service`**: Marks this class as a Spring service, allowing it to be automatically detected and managed by Spring's application context.
- **`@Autowired`**: Used to inject dependencies through the constructor.

## Logging

- **Logger**: Uses `LoggerFactory` to log important events, such as successful logins, failed logins, and errors during the authentication process.

## Security Context Management

- The `handleSuccessfulLogin` method sets the security context for authenticated users by using the `SecurityContextHolder`.
- The context is persisted to the HTTP session using the `HttpSessionSecurityContextRepository` to maintain the user's session state across requests.

## Cookie Management

- The `handleRememberMeCookie` method sets or deletes a "remember me" cookie to allow the user to stay logged in for up to 30 days.
- The cookie is marked as `HttpOnly` and `Secure` to enhance security, ensuring it is only sent over HTTPS and cannot be accessed via client-side scripts.

## Usage

The `LoginHandler` class is used for user authentication in the application, providing methods for logging in, handling successful and failed logins, managing security context, and ensuring that users are redirected appropriately based on their roles. It also includes support for "remember me" functionality to enhance user convenience.

---

[Back to System Overview](../system-overview.md)