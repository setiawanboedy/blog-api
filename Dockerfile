FROM gradle:8.8-jdk17-alpine AS build
WORKDIR /app
COPY . /app
RUN gradle build

# Gunakan image base JDK 17
# Stage 2: Runtime Stage
FROM openjdk:17-jdk-alpine
WORKDIR /app
COPY --from=build /app/build/libs/blog-api-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
