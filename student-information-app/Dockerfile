# Use an official Maven image to build the application
FROM maven:3.8.6-jdk-21 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml separately to leverage Docker cache
COPY pom.xml .

# Optionally, copy settings.xml if you have custom Maven settings
# COPY settings.xml /root/.m2/

# Resolve Maven dependencies
RUN mvn dependency:go-offline

# Copy the source code
COPY src /app/src

# Package the application
RUN mvn clean package -DskipTests

# Use a lightweight JRE image to run the application
FROM eclipse-temurin:21-jre

# Set metadata information
LABEL authors="bober"

# Copy the jar file from the build stage
COPY --from=build /app/target/student-information-app-1.0-SNAPSHOT.jar /app/app.jar

# Expose the application port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
