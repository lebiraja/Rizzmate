FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy Maven build artifacts (assumes Maven build has been run)
COPY target/ai-resume-builder-1.0.0.jar app.jar

# Expose port
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
