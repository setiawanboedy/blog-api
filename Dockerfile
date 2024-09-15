# Gunakan image base JDK 17
FROM openjdk:17-jdk-alpine

# Set working directory di dalam container
WORKDIR /app

# Salin build file JAR dari lokal ke container
COPY build/libs/blog-api-0.0.1-SNAPSHOT.jar /app/app.jar

# Expose port 8080 agar bisa diakses dari luar container
EXPOSE 8080

# Perintah untuk menjalankan aplikasi Spring Boot
ENTRYPOINT ["java", "-jar", "/app/app.jar"]


#FROM openjdk:17-jdk-slim
#RUN sudo ./gradlew build
#COPY build/libs/blog-api-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]