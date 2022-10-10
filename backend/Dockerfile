# syntax=docker/dockerfile:1
FROM gradle:7.5.1-jdk17-alpine AS jar
COPY . /app
WORKDIR /app
RUN gradle bootJar

FROM openjdk:19-jdk-alpine3.16
COPY --from=jar /app/api/build/libs/api-0.0.1-SNAPSHOT.jar app.jar
COPY --from=jar /app/create.sql create.sql
ENTRYPOINT ["java", "-Dspring.profiles.active=prod" ,"-jar", "/app.jar"]