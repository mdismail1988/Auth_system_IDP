FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /idp-service
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

#FROM openjdk:21-jdk-slim AS build
#WORKDIR /idp-service
#RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*
#COPY . .
#RUN ./mvnw clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /idp-service
COPY --from=build /idp-service/target/idp-service-1.0-SNAPSHOT.jar idp-service.jar
EXPOSE 9091
ENTRYPOINT ["java", "-jar", "idp-service.jar"]