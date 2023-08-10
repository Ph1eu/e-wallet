FROM openjdk:20-jdk
# Set the working directory inside the container
WORKDIR /app
# Copy the executable JAR into the container
COPY target/report-service-0.0.1-SNAPSHOT.jar app.jar

# Start the Spring Boot application
CMD ["java", "-jar", "app.jar"]
