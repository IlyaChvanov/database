FROM openjdk:23-jdk
WORKDIR /app
COPY build/libs/database-0.0.1-SNAPSHOT.jar /app/myapp.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "myapp.jar"]