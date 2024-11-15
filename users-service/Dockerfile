FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/users-service-0.0.1.jar
COPY ${JAR_FILE} app_users.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app_users.jar"]