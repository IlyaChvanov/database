FROM openjdk:17-jdk-slim
WORKDIR /app
COPY build/libs/database-0.0.1-SNAPSHOT.jar /app/myapp.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "myapp.jar"]