FROM eclipse-temurin:17-jdk
ARG JAR_FILE=target/config-server-0.0.1.jar
COPY ${JAR_FILE} app_config.jar
EXPOSE 8098
ENTRYPOINT ["java", "-jar", "app_config.jar"]