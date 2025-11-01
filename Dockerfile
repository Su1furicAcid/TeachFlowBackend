# Stage 1: Build with Gradle
FROM gradle:jdk17-focal AS build
WORKDIR /home/gradle/src
# Copy build files and download dependencies first to leverage caching
COPY build.gradle settings.gradle gradlew gradlew.bat ./
COPY gradle ./gradle
RUN gradle dependencies --no-daemon
COPY src ./src
# Build the application, skipping tests as they should be run in a separate CI step
RUN gradle build --no-daemon -x test

# Stage 2: Create the final image
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
# Copy the jar file from the build stage
COPY --from=build /home/gradle/src/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]

