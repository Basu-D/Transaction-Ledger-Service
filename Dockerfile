## Build stage
FROM gradle:7.6-jdk17 AS build
WORKDIR /app

# Copy Gradle files
COPY build.gradle settings.gradle ./
COPY gradle ./gradle

# Copy source code
COPY src ./src

# Build application
RUN gradle build --no-daemon


## Runtime stage
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the built fat JAR
COPY --from=build /app/build/libs/Transaction-Ledger-Service*.jar app.jar

EXPOSE 8080

# Run application
ENTRYPOINT ["java", "-jar", "app.jar"]

