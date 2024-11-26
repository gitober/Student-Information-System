# System Architecture

## Overview
The system follows a layered architecture pattern, integrating a **Vaadin frontend**, a **Spring Boot backend**, and a relational database for persistence. Below are the major components of the architecture:

## Application Layers
### Backend
- **Framework**: Spring Boot handles request routing, security, and business logic.
- **Repositories**: Spring Data JPA manages interactions with the database.
- **Security**: Spring Security enforces role-based access control.
- **DTOs and Services**:
    - **DTOs**: Facilitate data transfer between the backend and frontend.
    - **Services**: Encapsulate business logic for features like attendance, grades, and courses.

### Frontend
- **Framework**: Vaadin provides a declarative way to build the UI using Java.
- **Themes**: Custom themes ensure consistent styling across all pages.
- **Responsive Design**: Optimized for various devices.

### Database
- **Structure**: Uses tables such as `Users`, `Courses`, and `Attendance`.
- **Relationships**: Implements foreign key constraints to maintain referential integrity.

## Interaction Between Components
1. **Frontend to Backend**: The Vaadin UI sends user requests to REST controllers.
2. **Backend to Database**: Service classes fetch or update data using JPA repositories.
3. **Database to Frontend**: Data is retrieved, processed, and displayed in the UI.

## Deployment
- The application is containerized using **Docker** for consistent environments across development, staging, and production.

[Back to Project Overview](../project-overview/project-overview.md)