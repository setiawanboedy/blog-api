FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY build/libs/blog-api-0.0.1-SNAPSHOT-all.jar app.jar
# Menetapkan variabel environment PORT, Cloud Run akan menggantinya
ENV PORT=8080
ENV SPRING_DATASOURCE_URL=jdbc:mysql://34.134.92.189:3306/blog
ENV SPRING_DATASOURCE_USERNAME=blog
ENV SPRING_DATASOURCE_PASSWORD=Sinaga@2016
ENTRYPOINT ["java","-jar","/app.jar"]