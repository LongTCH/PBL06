# Use a base image with OpenJDK
FROM openjdk:17-jdk-slim as build

# Set the working directory
WORKDIR /app

# Copy the JAR file into the container (after building it via Maven or Gradle)
COPY target/clothes-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose the port your Spring Boot application is running on
EXPOSE 8080

# Command to run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]

