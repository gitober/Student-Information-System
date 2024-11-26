# Docker Documentation

## Contents
- [Introduction](#introduction)
- [Environment Variables](#environment-variables)
- [Dockerfile](#dockerfile)
- [Docker Compose](#docker-compose)
- [Using Docker Desktop](#using-docker-desktop)
- [Creating a Repository in Docker Hub](#creating-a-repository-in-docker-hub)
- [Testing and Troubleshooting](#testing-and-troubleshooting)

---

## Introduction

This guide explains how to use Docker to containerize and manage the Student Information System project. It includes:

- **Building the Docker Image**: Steps for creating a Docker image of the project.
- **Running Locally with Docker Desktop**: Testing and debugging the application locally.
- **Pushing to Docker Hub**: Sharing the Docker image in a centralized repository.
- **Automating Deployment with Jenkins**: Integrating Jenkins for building, testing, and deploying the application.

For detailed Jenkins pipeline instructions, see the Jenkins Documentation.

<p align="right">[<a href="#docker-documentation">back to top</a>]</p>

---

## Environment Variables

Environment variables manage sensitive information like database credentials. Create a `.env` file in the root directory with the following content:

```env
# Database settings
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
DB_URL=jdbc:mysql://host.docker.internal:3307/studentinfoapp
MYSQL_ROOT_PASSWORD=your_mysql_root_password
```

**Instructions**:
- Replace placeholders (`your_db_username`, etc.) with actual values.
- Add `.env` to your `.gitignore` file to prevent it from being committed to version control.

<p align="right">[<a href="#docker-documentation">back to top</a>]</p>

---

## Dockerfile

The Dockerfile defines the steps for building your application container using a multi-stage build for efficiency.

### Example Dockerfile

```dockerfile
# Build Stage
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml . 
RUN mvn dependency:go-offline
COPY src /app/src
RUN mvn clean package vaadin:build-frontend -Pproduction -DskipTests

# Run Stage
FROM eclipse-temurin:21-jre
LABEL authors="your_name"
RUN groupadd -r appuser && useradd --no-log-init -r -g appuser appuser
COPY --from=builder /app/target/student-information-app-1.0-SNAPSHOT.jar /app.jar
RUN chown appuser:appuser /app.jar
USER appuser
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

---

### Steps to Build and Run

1. Save the file as `Dockerfile` in the project root.
2. Build the image:

   ```sh
   docker build -t student-information-app .
   ```

3. Run the container:

   ```sh
   docker run -p 8090:8090 student-information-app
   ```

<p align="right">[<a href="#docker-documentation">back to top</a>]</p>

---

## Docker Compose

Docker Compose simplifies running multiple services (e.g., the application and database) simultaneously.

### Example `docker-compose.yml`

```yaml
version: '3.8'

services:
  mysql:
    image: mysql:8.0
    container_name: mysql_container
    environment:
      MYSQL_ROOT_PASSWORD: ${MYSQL_ROOT_PASSWORD}
      MYSQL_DATABASE: studentinfoapp
    ports:
      - "3307:3306"
    volumes:
      - mysql-data:/var/lib/mysql
    networks:
      - app-network

  studentinfoapp:
    image: your_username/studentinfoapp:latest
    container_name: app_container
    build:
      context: docker
    environment:
      - DB_URL=${DB_URL}
      - DB_USERNAME=${DB_USERNAME}
      - DB_PASSWORD=${DB_PASSWORD}
    ports:
      - "8090:8080"
    depends_on:
      - mysql
    networks:
      - app-network

volumes:
  mysql-data:

networks:
  app-network:
    driver: bridge
```
---

### Steps to Start and Stop Services

- Start services:

  ```sh
  docker-compose up --build
  ```

- Stop services:

  ```sh
  docker-compose down
  ```

<p align="right">[<a href="#docker-documentation">back to top</a>]</p>

---

## Using Docker Desktop

Docker Desktop offers a graphical interface to build and run containers locally.

---

### Steps to Build and Run

1. **Build Image**:
    - Open Docker Desktop.
    - Go to **Images** > **Build Image**.
    - Select your Dockerfile and context directory.

2. **Run Container**:
    - Locate your built image under **Images**.
    - Click **Run** and map ports:
        - **Host Port**: 8090
        - **Container Port**: 8080

3. **Test Application**:
    - Access [http://localhost:8090](http://localhost:8090) to verify the app is running.

<p align="right">[<a href="#docker-documentation">back to top</a>]</p>

---

## Creating a Repository in Docker Hub

Docker Hub allows you to share your images publicly or privately.

### Steps to Create a Repository

1. Log in to Docker Hub.
2. Click **Create Repository**.
3. Enter a name (e.g., `studentinfoapp`).
4. Choose visibility (Public/Private).
5. Click **Create**.

### Push Your Image

1. Tag the image:

   ```sh
   docker tag student-information-app your_username/studentinfoapp:latest
   ```

2. Push the image:

   ```sh
   docker push your_username/studentinfoapp:latest
   ```

<p align="right">[<a href="#docker-documentation">back to top</a>]</p>

## Testing and Troubleshooting

### Common Commands

- **View logs**:

  ```sh
  docker logs app_container
  ```

- **Monitor resource usage**:

  ```sh
  docker stats
  ```

- **List running containers**:

  ```sh
  docker ps
  ```

---

### Common Issues

- **Port Conflicts**:
    - Ensure ports 8090 and 3307 are free.
    - Use `netstat` to identify processes using these ports.

- **Environment Variable Errors**:
    - Verify `.env` file configuration.

- **Build Failures**:
    - Check dependencies and ensure the Dockerfile syntax is correct.

<p align="right">[<a href="#docker-documentation">back to top</a>]</p>

---

[Back to Project Overview](../project-overview/project-overview.md)

