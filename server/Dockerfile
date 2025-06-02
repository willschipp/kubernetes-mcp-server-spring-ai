# Stage 1: Build the application with Gradle
FROM gradle:8.14-jdk21 AS builder

WORKDIR /app
COPY . .
RUN gradle clean build

# Stage 2: Runtime image
FROM eclipse-temurin:21

WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar

ENTRYPOINT ["java", "-jar", "app.jar"]
