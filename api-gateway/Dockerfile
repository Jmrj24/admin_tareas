FROM eclipse-temurin:17-jdk
ARG JAR_FILE=target/api-gateway-0.0.1.jar
COPY ${JAR_FILE} app_gateway.jar
EXPOSE 443
ENTRYPOINT ["java", "-jar", "app_gateway.jar"]