# Test Suite Overview

This document outlines the testing strategy and methodologies used in the project, focusing on the key areas of the application: controllers, data (including entities and repositories), services, security, and views. It provides insight into how we ensure the functionality, reliability, and robustness of the application.

## Contents
- [Introduction](#introduction)
- [Testing Focus Areas](#testing-focus-areas)
    - [Controllers](#1-controllers)
    - [Data (Entities and Repositories)](#2-data-entities-and-repositories)
    - [Services](#3-services)
    - [Security](#4-security)
    - [Views (UI Components)](#5-views-ui-components)
    - [Utils and Helpers](#6-utils-and-helpers)
- [Testing Approach](#testing-approach)
- [Testing Methodologies](#testing-methodologies)
    - [Mocking with Mockito](#mocking-with-mockito)
    - [Assertions](#assertions)
    - [Reflection](#reflection)
    - [Simulating User Interactions](#simulating-user-interactions)
    - [Setup and Teardown](#setup-and-teardown)
- [Annotations Used](#annotations-used)
    - [`@DataJpaTest`](#1-datajpatest)
    - [`@Transactional`](#2-transactional)
    - [`@ActiveProfiles("test")`](#3-activeprofilestest)
    - [`@WebMvcTest(controllers = UserController.class)`](#4-webmvctestcontrollers-usercontrollerclass)
    - [`@AutoConfigureMockMvc(addFilters = false)`](#5-autoconfiguremockmvcaddfilters--false)
    - [`@ExtendWith(MockitoExtension.class)`](#6-extendwithmockitoextensionclass)
    - [`@SpringBootTest`](#7-springboottest)
- [Test Configuration](#test-configuration)
    - [`application-test.properties`](#application-testproperties)

## Introduction

The project's test suite is designed to cover a wide range of functionalities, from data persistence and service layer logic to user interface components and security mechanisms. Our goal is to create a stable, maintainable application by employing unit tests that validate individual components, integration tests that ensure cohesive operation across layers, and mock testing to simulate complex interactions.

## Testing Focus Areas

### 1. Controllers

The controllers act as intermediaries between the frontend and backend, handling requests and responses. We test controllers to ensure they:
- Correctly handle HTTP requests and return appropriate responses.
- Interact with service layers as expected.
- Manage security and user authentication where necessary.

### 2. Data (Entities and Repositories)

Data is at the heart of the application. Our testing covers:
- **Entities:** Ensuring proper mappings, relationships, and constraints between data models.
- **Repositories:** Verifying CRUD operations and custom queries using mock databases, ensuring data integrity and correct transaction handling.

### 3. Services

The service layer contains the core business logic of the application. We test services to:
- Validate methods that process data and interact with repositories.
- Ensure data consistency and correctness.
- Verify interactions between services and external dependencies using mocks.

### 4. Security

Security is crucial, especially for handling user data and authentication. Security tests focus on:
- Validating user authentication and role-based access control.
- Ensuring secure data handling in login and logout processes.
- Simulating different user roles (e.g., students, teachers) to verify access restrictions.

### 5. Views (UI Components)

The views are the user interface components built with Vaadin. Our tests for views:
- Ensure the presence and layout of UI components (e.g., fields, buttons, grids).
- Simulate user interactions, such as button clicks and form submissions.
- Verify that UI components interact correctly with the underlying services.

### 6. Utils and Helpers

These classes provide utility functions and helper methods used across the application.

## Testing Approach

Our test suite primarily consists of unit tests and integration tests:
- **Unit Tests:** These validate the smallest units of code, such as service methods, repository operations, and view components. We use mocking extensively to isolate the components being tested, ensuring that tests are fast and focused.
- **Integration Tests:** These involve interaction between multiple components, such as a service calling a repository to fetch data. These tests check if the components work together as expected, focusing on data flow and business logic.

## Testing Methodologies

### Mocking with Mockito

- Most tests employ Mockito to create mock instances of services, repositories, and UI components. This allows us to simulate complex interactions and behaviors without requiring a real database or an actual running server.
- For example, we mock `AuthenticatedUser` to simulate logged-in users and test different user roles' access to views.

### Assertions

- We use assertions to validate the expected outcomes of various actions. For example, we assert that a form submission triggers the correct service method or that a UI component is present in the view.

### Reflection

- Some tests involve accessing private fields in classes (such as UI components in views) using reflection. This technique allows us to directly interact with components that are not publicly exposed, verifying their state and behavior.

### Simulating User Interactions

- Tests for views simulate user actions, such as filling in text fields, clicking buttons, and selecting options in grids. This helps ensure that the UI behaves correctly and that user interactions trigger the intended backend processes.

### Setup and Teardown

- Each test class uses `@BeforeEach` to set up a clean environment before every test, ensuring independence between tests.
- The `@AfterEach` annotation is used to reset the state, including closing any mock resources, to avoid any side effects between tests.

## Annotations Used

### 1. `@DataJpaTest`

- Used for testing JPA repositories. It configures an in-memory database, scans for `@Entity` classes, and configures Spring Data JPA repositories.
- **Why:** To verify the correctness of repository operations in isolation from the rest of the application.

### 2. `@Transactional`

- Ensures that each test method runs within a transaction that is rolled back after the method completes.
- **Why:** To prevent tests from affecting each other by leaving behind residual data in the database.

### 3. `@ActiveProfiles("test")`

- Specifies the profile to use when running the tests.
- **Why:** To load test-specific configurations, such as `application-test.properties`.

### 4. `@WebMvcTest(controllers = UserController.class)`

- Used to test Spring MVC controllers, configuring only the components required for the web layer.
- **Why:** To isolate controller testing from other layers of the application, such as services or repositories.

### 5. `@AutoConfigureMockMvc(addFilters = false)`

- Automatically configures `MockMvc` for testing the web layer, optionally disabling security filters.
- **Why:** To test controllers without interference from security mechanisms.

### 6. `@ExtendWith(MockitoExtension.class)`

- Integrates Mockito into JUnit 5, allowing for the creation and injection of mocks.
- **Why:** To use Mockito's mocking capabilities for testing services, controllers, and other components.

### 7. `@SpringBootTest`

- Used for integration testing, loading the full application context.
- **Why:** To verify that all components of the application work together as expected.

## Test Configuration

### `application-test.properties`

The test environment employs a dedicated `application-test.properties` file to configure the H2 in-memory database and Hibernate settings, tailoring both to the specific needs of the testing profile. H2 is a lightweight, in-memory database ideal for testing as it allows quick setup and teardown of databases without persisting data. Hibernate, an Object-Relational Mapping (ORM) framework, automates the mapping between Java objects and database tables, handling SQL generation, and schema creation, which ensures that entities are persisted correctly during tests.

```properties
# H2 Database configuration for tests
spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# Hibernate settings for testing
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

# Disable SQL initialization
spring.sql.init.mode=never

# Test-specific logging configuration (optional)
logging.level.org.springframework=INFO

# Email settings for tests (specifying dummy values for SMTP server)
EMAIL_USERNAME=dummy_email@test.com
EMAIL_PASSWORD=dummy_password
spring.mail.host=smtp.test.com
spring.mail.port=587
spring.mail.properties.mail.smtp.auth=false
spring.mail.properties.mail.smtp.starttls.enable=false
