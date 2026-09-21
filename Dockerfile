# Use Java 17 to run the Spring Boot application
FROM eclipse-temurin:17-jdk

# Set working directory inside container
WORKDIR /app

# Copy Maven build JAR into the container
COPY target/employee-management-api-0.0.1-SNAPSHOT.jar app.jar

# Application runs on port 8080
EXPOSE 8080

# Start Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar"]