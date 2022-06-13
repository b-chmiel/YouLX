# syntax=docker/dockerfile:1
FROM gradle:7.4.2-jdk17-alpine AS jar
COPY . /app
WORKDIR /app
RUN gradle bootJar

FROM openjdk:17-alpine3.14
COPY --from=jar /app/api/build/libs/*.jar app.jar
COPY --from=jar /app/create.sql create.sql
ENTRYPOINT ["java", "-Dspring.profiles.active=prod" ,"-jar", "/app.jar"]