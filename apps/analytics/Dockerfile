FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY analytics/pom.xml .
RUN mvn dependency:go-offline
COPY analytics/src /app/src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
COPY --from=build /app/target/*.jar  app.jar
ENTRYPOINT ["java","--add-exports","java.base/sun.nio.ch=ALL-UNNAMED","-jar","app.jar"]