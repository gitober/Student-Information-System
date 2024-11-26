# SecurityConfiguration Class Documentation

## Purpose

The `SecurityConfiguration` class is responsible for configuring the security settings of the application, including authentication, authorization, session management, and other security-related aspects. It extends `VaadinWebSecurity` to integrate Spring Security with Vaadin.

## Dependencies

- **UserDetailsServiceImpl**: A custom implementation of `UserDetailsService` used to load user-specific data during authentication.
- **DataSource**: Used to configure the persistent token repository for "remember-me" functionality.

## Fields

- **userDetailsService (UserDetailsServiceImpl)**: Provides user details for authentication purposes.
- **dataSource (DataSource)**: Provides database connectivity for storing persistent login tokens.
- **logger (Logger)**: Logger instance for logging messages related to security configurations.

## Methods

### Bean Methods

- **passwordEncoder()**: Provides a `PasswordEncoder` bean that uses BCrypt to hash passwords.
- **authenticationProvider()**: Configures the `DaoAuthenticationProvider` to use the custom `UserDetailsService` and `PasswordEncoder`.
- **authenticationManager(AuthenticationConfiguration authConfig)**: Provides the `AuthenticationManager` bean, used to manage authentication processes.
- **securityContextRepository()**: Provides the `SecurityContextRepository` bean, which stores the security context in the HTTP session.
- **persistentTokenRepository()**: Configures a `PersistentTokenRepository` bean for handling "remember-me" tokens using a JDBC token repository.

### Overridden Method

- **configure(HttpSecurity http)**: Configures HTTP security settings, such as session management, security context, authorization requests, form login, "remember-me" functionality, logout, authentication provider, exception handling, and anonymous access.
    - **Session Management**: Configures session creation policy as `IF_REQUIRED` and logs the configuration phase.
    - **Security Context**: Sets the custom `securityContextRepository` and logs the configuration phase.
    - **Authorization Requests**: Configures which endpoints are accessible without authentication and which require a specific role.
    - **Form Login**: Configures the login page URL, default success URL, and username parameter.
    - **Remember-Me**: Configures the "remember-me" functionality, specifying the token repository, validity period, and parameter.
    - **Logout**: Configures logout settings, including URL, session invalidation, cookie deletion, and success handler.
    - **Exception Handling**: Configures the authentication entry point for handling unauthenticated access attempts.
    - **Anonymous Access**: Configures anonymous users to have `ROLE_ANONYMOUS` authority.

### Utility Method

- **logCurrentSecurityContext(String phase)**: Logs the current authentication details from the `SecurityContextHolder` during different phases of the security configuration.

## Annotations

- **@Configuration**: Marks this class as a configuration class, indicating that it provides Spring beans.
- **@EnableWebSecurity**: Enables Spring Security for the application.
- **@Bean**: Marks methods that produce a bean to be managed by the Spring container.

## Usage

The `SecurityConfiguration` class is used to define the security rules and settings for the application, including how users authenticate, how sessions are managed, and how different endpoints are secured. It integrates Vaadin with Spring Security to provide seamless security handling across the application.

---

[Back to Code Structure Overview](../../code-structure/code-structure.md)