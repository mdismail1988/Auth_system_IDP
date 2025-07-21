FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /auth-gateway-service
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

#FROM openjdk:21-jdk-slim AS build
#WORKDIR /auth-gateway-service
#RUN ./mvnw clean package -DskipTests
#RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
#COPY . .
#RUN mvnw clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /auth-gateway-service
COPY --from=build /auth-gateway-service/target/auth-gateway-service-0.0.1-SNAPSHOT.jar auth-gateway-service.jar
EXPOSE 9090
ENTRYPOINT ["java", "-jar", "auth-gateway-service.jar"]