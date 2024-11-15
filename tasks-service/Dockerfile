FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/tasks-service-0.0.1.jar
COPY ${JAR_FILE} app_tasks.jar
EXPOSE 8092
ENTRYPOINT ["java", "-jar", "app_tasks.jar"]