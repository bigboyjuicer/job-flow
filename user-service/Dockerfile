FROM maven:3.9-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
COPY common/pom.xml common/
COPY user-service/pom.xml user-service/

RUN mvn -pl user-service -am dependency:go-offline -B

COPY common/src common/src
COPY user-service/src user-service/src

RUN mvn -pl user-service -am clean package -DskipTests -B

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app


COPY --from=build /app/user-service/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]