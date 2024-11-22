# Settings Overview

## Contents
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation](#installation)
- [Configuration Files](#configuration-files)
  - [.env File](#env-file)
  - [Docker .env File](#docker-env-file)
  - [application.properties](#application-properties)
  - [Dockerfile](#dockerfile)
  - [Jenkins Pipeline](#jenkins-pipeline)
- [Database Overview](#database-overview)
- [Additional Configurations](#additional-configurations)
  - [Localization](#localization)
  - [Email Service](#email-service)
  - [Security Configuration](#security-configuration)
  - [Environment Profiles](#environment-profiles)

---

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 21
- Maven
- MySQL
- Docker (Optional for containerized deployment)


### Installation
- Clone the repository and navigate to the project directory.
- Configure the `.env` file in the `src/main/resources` folder.
- Build the project using Maven.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

## Configuration Files

### .env File (Sensitive information hidden)
```plaintext
# Database settings
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
DB_URL=jdbc:mysql://localhost:3306/studentinfoapp

# Email settings
EMAIL_USERNAME=your_email
EMAIL_PASSWORD=your_email_password
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
```

### Docker .env File
This `.env` file is located under the `student-information-app` folder and is used for Docker to configure the application.
```plaintext
# Database settings
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
DB_URL=jdbc:mysql://host.docker.internal:3307/studentinfoapp
MYSQL_ROOT_PASSWORD=your_mysql_root_password

# Email settings (if required for application)
EMAIL_USERNAME=your_email
EMAIL_PASSWORD=your_email_password
SMTP_HOST=smtp.gmail.com
SMTP_PORT=587
```

### `application.properties` Highlights
```properties
# Server port
server.port=${PORT:8080}

# Database and JPA settings
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# Email settings
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=${EMAIL_USERNAME}
spring.mail.password=${EMAIL_PASSWORD}
```

### Dockerfile
The project uses a multi-stage Dockerfile to build and run the application efficiently. See the Dockerfile for the detailed setup, including:
- Using `maven:3.9.4-eclipse-temurin-21` as a base image for the build stage.
- Running the compiled JAR file using a lightweight JRE image for the runtime stage.

### Jenkins Pipeline
The Jenkins pipeline automates code checkout, build, testing, and code coverage report generation. The pipeline is designed for Continuous Integration and Continuous Delivery (CI/CD) with:
- Steps for building the application.
- Running test cases.
- Generating code coverage reports for quality assurance.

See the pipeline script (`Jenkinsfile`) in the root directory for more details.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

## Database Overview
For more details on the database structure and schema, please refer to the [database-overview](../database/database-overview.md) file in the `database` folder. This includes a comprehensive explanation of all tables, relationships, and database configurations used in the **Student Information System**.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

## Additional Configurations

### Localization
- The application supports multiple languages, including **English**, **Finnish**, **Russian**, and **Chinese**.
- Language settings are configurable via `LocalizationConfig` to provide a seamless experience for users in different regions.

### Email Service
- The email configuration is set in both the `.env` and `application.properties` files to enable sending notifications for **password reset** or **user registration**.

### Security Configuration
- **Spring Security** is used to manage user authentication and authorization.
- Custom security settings are managed in `SecurityConfiguration` under the `code-reference/security` folder, which handles roles and permission levels.

### Environment Profiles
- The application uses profiles such as **dev**, **test**, and **prod** to manage different configurations for local development, testing, and production environments.
- Profiles can be activated by using the `spring.profiles.active` property.

<p align="right">(<a href="#contents">back to top</a>)</p>

---

[Back to Project Overview](../project-overview.md)

