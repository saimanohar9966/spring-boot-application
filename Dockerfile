# Use the official Maven image as the build environment
FROM maven:3.8.6-jdk-11 as builder

# Set the working directory in the Docker image
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use OpenJDK for the runtime
FROM openjdk:11-jre-slim

# Copy the built artifact from the previous stage
COPY --from=builder /app/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]

# Expose the port the app runs on
EXPOSE 8080
