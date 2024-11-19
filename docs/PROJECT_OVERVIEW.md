# Student Information System - Overall Documentation

## Contents
- [Overview](#overview)
- [Application Architecture](#application-architecture)
    - [Backend](#backend)
    - [Frontend](#frontend)
- [Index Catalog](#index-catalog)
    - [Configuration](#configuration)
    - [Controllers](#controllers)
    - [Data](#data)
        - [DTOs](#dtos)
        - [Entities](#entities)
        - [Repositories](#repositories)
    - [Security](#security)
    - [Services](#services)
    - [Utilities](#utilities)
    - [Views](#views)
- [Key Components and Their Interaction](#key-components-and-their-interaction)
- [Custom CSS and Themes](#custom-css-and-themes)
- [Localization](#localization)
- [Summary](#summary)

## Index Catalog

### Configuration
- [LocalizationConfig](config/LocalizationConfig.md)

### Controllers
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

#### DTOs
- [CourseCreationRequestDTO](data/dto/CourseCreationReaquestDTO.md)
- [RegistrationDTO](data/dto/RegistrationDTO.md)

#### Entities
- [AbstractEntity](data/entity/AbstractEntity.md)
- [Attendance](data/entity/Attendance.md)
- [Course](data/entity/Course.md)
- [CourseTranslation](data/entity/CourseTranslation.md)
- [Department](data/entity/Department.md)
- [DepartmentTranslation](data/entity/DepartmentTranslation.md)
- [Enrollment](data/entity/Enrollment.md)
- [Grade](data/entity/Grade.md)
- [Language](data/entity/Language.md)
- [Registration](data/entity/Registration.md)
- [Role](data/entity/Role.md)
- [Student](data/entity/Student.md)
- [Subject](data/entity/Subject.md)
- [SubjectTranslation](data/entity/SubjectTranslation.md)
- [Teacher](data/entity/Teacher.md)
- [TeacherTranslation](data/entity/TeacherTranslation.md)
- [User](data/entity/User.md)
- [UserTranslation](data/entity/UserTranslation.md)

#### Repositories
- [AttendanceRepository](data/repository/AttendanceRepository.md)
- [CourseRepository](data/repository/CourseRepository.md)
- [CourseTranslationRepository](data/repository/CourseTranslationRepository.md)
- [DepartmentRepository](data/repository/DepartmentRepository.md)
- [DepartmentTranslationRepository](data/repository/DepartmentTranslationRepository.md)
- [GradeRepository](data/repository/GradeRepository.md)
- [RegistrationRepository](data/repository/RegistrationRepository.md)
- [StudentRepository](data/repository/StudentRepository.md)
- [SubjectRepository](data/repository/SubjectRepository.md)
- [SubjectTranslationRepository](data/repository/SubjectTranslationRepository.md)
- [TeacherRepository](data/repository/TeacherRepository.md)
- [TeacherTranslationRepository](data/repository/TeacherTranslationRepository.md)
- [UserRepository](data/repository/UserRepository.md)
- [UserTranslationRepository](data/repository/UserTranslationRepository.md)

### Security
- [AuthenticatedUser](security/AuthenticatedUser.md)
- [SecurityConfiguration](security/SecurityConfiguration.md)
- [UserDetailsServiceImpl](security/UserDetailsServiceImpl.md)

### Services
- [AttendanceService](services/AttendanceService.md)
- [CourseService](services/CourseService.md)
- [DateService](services/DateService.md)
- [DepartmentService](services/DepartmentService.md)
- [EmailService](services/EmailService.md)
- [GradeService](services/GradeService.md)
- [SecurityService](services/SecurityService.md)
- [StudentService](services/StudentService.md)
- [SubjectService](services/SubjectService.md)
- [TeacherService](services/TeacherService.md)
- [TranslationService](services/TranslationService.md)
- [UserContentLoader](services/UserContentLoader.md)
- [UserService](services/UserService.md)

### Utilities
- [LoginHandler](utils/LoginHandler.md)
- [RegistrationHandler](utils/RegistrationHandler.md)

### Views
- **Course Views**: [CourseViews](views/course/CourseViews.md)
- **Edit Profile Views**: [EditProfileViews](views/editprofile/EditProfileViews.md)
- **Forgot Password View**: [ForgotPasswordView](views/forgotpassword/ForgotPasswordView.md)
- **Grades Views**: [GradesViews](views/grade/GradesViews.md)
- **Header View**: [HeaderView](views/header/HeaderView.md)
- **Home Profile Page Views**: [HomeProfilePageViews](views/homeprofilepage/HomeProfilePageViews.md)
- **Login Page View**: [LoginPageView](views/loginpage/LoginPageView.md)
- **Logout View**: [LogoutView](views/logout/LogoutView.md)
- **Main Layout View**: [MainLayout](views/mainlayout/MainLayout.md)
- **Registration View**: [RegistrationView](views/registration/RegistrationView.md)
- **Teacher Attendance View**: [TeacherAttendanceView](views/teacher_attendance_view/TeacherAttendanceView.md)
- **Teacher Update Student Profile View**: [TeacherUpdateStudentProfileView](views/teacher_update_student_profile/TeacherUpdateStudentProfileView.md)



## Overview

The **Student Information System** is a Vaadin-based web application that manages student-related information, including courses, attendance, grades, and user profiles. The application offers distinct features for **students**, **teachers**, and **administrative roles**. It uses **Spring Boot** for backend services, managing persistence and business logic, and **Vaadin** for building the user interface.

## Application Architecture

### Backend
- **Spring Boot**: Manages server-side functionality, including security, business logic, and integration with databases.
- **JPA Repositories**: Handles interactions with the database, performing CRUD operations on core entities.
- **Service Layer**: Contains business logic to ensure consistency and coordinate multiple repository calls.

### Frontend
- **Vaadin**: Provides all the views of the application, allowing users to interact with the system using various UI components.
- **Custom CSS Designs**: Views are customized using CSS files located in `Frontend/themes/studentinformationapp/views` for consistent and polished user experience.

## Index Catalog

### Configuration
- **docs/config**: Application properties and setup configuration files.

### Controllers
- **docs/controllers**: Controllers for managing requests and routing them to services.

### Data
- **docs/data/dto**: Data Transfer Objects (DTOs) for data exchange between layers.
- **docs/data/entity**: Entity classes representing database tables.
- **docs/data/repository**: Repositories for interacting with the database.

### Security
- **docs/security**: Contains configurations and classes related to authentication and authorization.

### Services
- **docs/services**: Service classes implementing business logic.

### Utilities
- **docs/utils**: General-purpose utility classes for various application tasks.

### Views
- **docs/views**: Subdirectories for each application view, including login, registration, course management, attendance, grades, and more.

## Key Components

### Layout and Navigation
- **MainLayout** serves as the primary container for all views and provides consistent navigation for logged-in users.
- **RouterLayout and Composite**: Used to structure the views and manage the navigation consistently.

### Entities and DTOs
- **Entities**: Represent core data models such as **Student**, **Teacher**, **Attendance**, and **Course**.
- **DTOs (Data Transfer Objects)**: Used to transfer data between different application layers.

### Services and Repositories
- **Service Classes**: Business logic resides in service classes, which orchestrate operations between repositories and views.
- **Repositories**: Provide the interface to interact with the underlying database.

### Security
- **Spring Security**: Handles authentication and authorization, ensuring appropriate access control to views and resources.

## Custom CSS and Themes
- Custom styles are applied to ensure a consistent, visually pleasing experience across all views. These styles are located in the `Frontend/themes/studentinformationapp/views` directory.

## Localization
- The application supports multiple languages through **MessageSource** and locale settings. Users can select their preferred language, which is saved as a cookie for future sessions.

## Summary
The **Student Information System** is a comprehensive web application that effectively integrates:
- **Vaadin** for frontend UI,
- **Spring Boot** for backend functionality and security,
- **Service Classes** to connect the user interface with data management.

It provides a maintainable and scalable solution to manage student data, tailored to the needs of students, teachers, and administrators while maintaining a clear separation of concerns through well-structured layers.
