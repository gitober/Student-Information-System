# Use an official Maven image to build the application
FROM maven:latest AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code to the container
COPY pom.xml /app/
COPY src /app/src

# Package the application
RUN mvn clean package

# Use a lightweight JRE image to run the application
FROM eclipse-temurin:21-jre

# Set metadata information
LABEL authors="bober"

# Copy the jar file from the build stage
COPY --from=build /app/target/*.jar /app/app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
