# Test Suite Overview

This document outlines the testing strategy and methodologies used in the project, focusing on the key areas of the application: controllers, data (entities and repositories), services, security, and views. All tests are designed to be run directly within IntelliJ using JUnit 5, leveraging IntelliJ's powerful testing features for efficient development and debugging.

## Contents
- [Introduction](#introduction)
- [Testing in IntelliJ](#testing-in-intellij)
  - [Running Tests in IntelliJ](#running-tests-in-intellij)
  - [JUnit 5 Integration](#junit-5-integration)
- [Testing Focus Areas](#testing-focus-areas)
  - [Controllers](#controllers)
  - [Data (Entities and Repositories)](#data-entities-and-repositories)
  - [Services](#services)
  - [Security](#security)
  - [Views (UI Components)](#views-ui-components)
  - [Utils and Helpers](#utils-and-helpers)
- [Testing Methodologies](#testing-methodologies)
- [Test Configuration](#test-configuration)
- [Annotations Used](#annotations-used)

---

## Introduction
The project's test suite is designed to cover a wide range of functionalities, from data persistence and service layer logic to user interface components and security mechanisms. Testing is performed using JUnit 5 and executed seamlessly in IntelliJ IDEA, enabling developers to validate components quickly and interactively.

---

## Testing in IntelliJ

### Running Tests in IntelliJ
**From the Test Class:**
- Open any test file in the `src/test/java` folder.
- Click the green "Run" button next to the class or method name to execute the tests.

**From the Project View:**
- Navigate to the `src/test/java` directory in the Project View.
- Right-click on a test class or package and select **Run Tests** to execute all tests within the selected scope.

**Debugging Tests:**
- Use the **Debug** option available in IntelliJ to step through your tests interactively, inspect variables, and resolve issues.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

### JUnit 5 Integration
All tests are written using JUnit 5 (JUnit Jupiter). IntelliJ provides built-in support for JUnit 5, allowing for smooth execution and debugging of unit and integration tests. Tests can also be parameterized for flexibility and reusability.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

## Testing Focus Areas

### 1. Controllers
Controllers serve as the interface between the user and the backend. Tests for controllers include:
- Verifying HTTP request handling.
- Ensuring appropriate status codes and responses.
- Testing security filters for role-based access control.

### 2. Data (Entities and Repositories)
Tests ensure the integrity of database operations:
- **Entities**: Validate entity mappings, relationships, and constraints.
- **Repositories**: Verify CRUD operations using mock or in-memory databases like H2.

### 3. Services
The service layer encapsulates business logic. Tests validate:
- Data transformation and processing.
- Correct interactions between services and repositories.
- Use of mocks for external dependencies.

### 4. Security
Security tests focus on:
- Verifying authentication mechanisms.
- Testing role-based access restrictions.
- Simulating different user roles (e.g., teacher, student).

### 5. Views (UI Components)
View tests ensure a seamless user experience by:
- Verifying the presence and layout of components.
- Simulating user actions (e.g., clicks, form submissions).
- Testing integration between views and services.

### 6. Utils and Helpers
Utility classes and helper methods are tested to ensure:
- Consistent behavior across the application.
- Correct output for edge cases.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

## Testing Methodologies

### Mocking with Mockito
Mockito is used extensively for mocking services, repositories, and security components.
- Example: Simulating a logged-in user by mocking `AuthenticatedUser`.

### Assertions
Assertions are used to validate expected outcomes, such as correct responses or UI interactions. IntelliJ's Run Dashboard provides a clear view of assertion failures for debugging.

### Simulating User Interactions
For view components, tests simulate user inputs like typing into fields or clicking buttons to ensure the backend handles them correctly.

### Setup and Teardown
Test classes use `@BeforeEach` and `@AfterEach` to initialize and clean up resources, ensuring tests are isolated and repeatable.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

## Test Configuration

### application-test.properties
The test environment is configured with a lightweight H2 in-memory database for fast setup and teardown. IntelliJ loads this configuration automatically when running tests with the test profile.

```properties
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1
spring.datasource.driver-class-name=org.h2.Driver
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.sql.init.mode=never
```

<p align="right">(<a href="#contents">back to top</a>)</p>

---

## Annotations Used
- `@SpringBootTest`: Loads the complete application context for integration testing.
- `@ExtendWith(MockitoExtension.class)`: Enables Mockito for unit tests.
- `@DataJpaTest`: Configures an in-memory database for repository testing.
- `@WebMvcTest`: Isolates controller testing with mocked service layer dependencies.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

[Back to Testing Overview](../testing-overview.md)

