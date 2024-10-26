FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/comment-service-0.0.1-SNAPSHOT.jar /app/comment-service.jar

EXPOSE 8080

RUN apk add --no-cache curl

ENTRYPOINT ["java", "-jar", "comment-service.jar"]