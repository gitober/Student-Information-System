# Student Information System - Code Reference

## Contents
- [Overview](#overview)
- [Application Architecture](#application-architecture)
  - [Backend](#backend)
  - [Frontend](#frontend)
- [Key Components](#key-components)
  - [Layout and Navigation](#layout-and-navigation)
  - [Entities and DTOs](#entities-and-dtos)
  - [Services and Repositories](#services-and-repositories)
  - [Security](#security)
- [Custom CSS and Themes](#custom-css-and-themes)
- [Localization](#localization)
- [Summary](#summary)
- [Index Catalog](#index-catalog)
  - [Configuration](#configuration)
  - [Controllers](#controllers)
  - [Data](#data)
  - [Security](#security)
  - [Services](#services)
  - [Utilities](#utilities)
  - [Views](#views)

---

## Overview

The **Student Information System** is a Vaadin-based web application used for managing student-related activities, such as **courses**, **attendance**, **grades**, and **profiles**. It serves **students**, **teachers**, and **administrators** and uses **Spring Boot** for backend services and **Vaadin** for the user interface.

---

## Application Architecture

### Backend
- **Spring Boot**: Handles security, business logic, and database integration.
- **JPA Repositories**: Manages database operations for entities.
- **Service Layer**: Encapsulates business logic and integrates repository operations.


### Frontend
- **Vaadin**: Implements the UI and handles user interaction.
- **Custom CSS Designs**: Provides consistent styling across the application.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

## Key Components

### Layout and Navigation
- **MainLayout**: Manages the overall page structure and provides navigation links.


### Entities and DTOs
- **Entities**: Represent tables like **Student**, **Teacher**, **Course**, etc.
- **DTOs**: Used for data transfer between different layers of the application.


### Services and Repositories
- **Services**: Contain business logic connecting repositories to UI components.
- **Repositories**: Directly interact with the database for **CRUD** operations.


### Security
- **Spring Security**: Ensures role-based access and secure interaction.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

## Custom CSS and Themes
Custom styles in `Frontend/themes/studentinformationapp/views` provide consistent UI elements across all pages, ensuring a unified and visually pleasing experience for users.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

## Localization
The application supports **English**, **Finnish**, **Russian**, and **Chinese** through locale settings and translation files. This allows users from different regions to interact with the system in their preferred language.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

## Summary
The **Student Information System** integrates **Vaadin** for the frontend, **Spring Boot** for the backend, and **Spring Security** for secure access. The application offers a modular structure to handle user registration, course management, grades, and more, providing a scalable and maintainable solution for educational institutions.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

## Index Catalog

### Configuration
- **docs/config**: Application properties and setup configurations.
  - [LocalizationConfig](config/LocalizationConfig.md): Configuration related to localization.

### Controllers
- **docs/controllers**: Manages incoming requests and directs them to appropriate services.
  - [AttendanceController](controllers/AttendanceController.md)
  - [CourseController](controllers/CourseController.md)
  - [DepartmentController](controllers/DepartmentController.md)
  - [GradeController](controllers/GradeController.md)
  - [LogoutController](controllers/LogoutController.md)
  - [StudentController](controllers/StudentController.md)
  - [SubjectController](controllers/SubjectController.md)
  - [TeacherController](controllers/TeacherController.md)
  - [UserController](controllers/UserController.md)

### Data
- **docs/data**: Core data models and data access logic.
  - **DTOs**: Data Transfer Objects to transfer data between layers.
    - [CourseCreationRequestDTO](data/dto/CourseCreationReaquestDTO.md)
    - [RegistrationDTO](data/dto/RegistrationDTO.md)
  - **Entities**: Represents database schema and entities.
    - [Student](data/entity/Student.md)
    - [Course](data/entity/Course.md)
    - [Grade](data/entity/Grade.md)
    - [Attendance](data/entity/Attendance.md)
  - **Repositories**: Data access layer for CRUD operations.
    - [StudentRepository](data/repository/StudentRepository.md)
    - [CourseRepository](data/repository/CourseRepository.md)

### Security
- **docs/security**: Implements authentication and authorization.
  - [AuthenticatedUser](security/AuthenticatedUser.md)
  - [SecurityConfiguration](security/SecurityConfiguration.md)

### Services
- **docs/services**: Implements core business logic.
  - [AttendanceService](services/AttendanceService.md)
  - [CourseService](services/CourseService.md)
  - [StudentService](services/StudentService.md)


### Utilities
- **docs/utils**: General-purpose helper functions.
  - [LoginHandler](utils/LoginHandler.md)
  - [RegistrationHandler](utils/RegistrationHandler.md)

### Views
- **docs/views**: User interface views for different features.
  - [Course Views](views/course/CourseViews.md)
  - [Edit Profile Views](views/editprofile/EditProfileViews.md)
  - [Forgot Password View](views/forgotpassword/ForgotPasswordView.md)
  - [Grades Views](views/grade/GradesViews.md)

<p align="right">(<a href="#contents">back to top</a>)</p>


---

[Back to Project Overview](../../project-overview/project-overview.md)

