# Use a Maven image with JDK 21 to build the application
FROM maven:3.9.4-eclipse-temurin-21 AS builder

# Set the working directory
WORKDIR /app

# Copy the pom.xml separately to leverage Docker cache
COPY pom.xml .

# Resolve Maven dependencies
RUN mvn dependency:go-offline

# Copy the source code
COPY src /app/src

# Build the Vaadin frontend and package the application in production mode
RUN mvn clean package vaadin:build-frontend -Pproduction -DskipTests

# Use a lightweight JRE image to run the application
FROM eclipse-temurin:21-jre

# Set metadata information
LABEL authors="bober"

# Copy the jar file and any required frontend resources from the build stage
COPY --from=builder /app/target/student-information-app-1.0-SNAPSHOT.jar /app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app.jar"]
