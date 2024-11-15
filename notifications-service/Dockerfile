FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/notifications-service-0.0.1.jar
COPY ${JAR_FILE} app_notifications.jar
EXPOSE 8099
ENTRYPOINT ["java", "-jar", "app_notifications.jar"]