FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY stream-filter/pom.xml .
RUN mvn dependency:go-offline
COPY stream-filter/src /app/src
RUN mvn clean package -DskipTests

#FROM eclipse-temurin:17-jre-alpine
FROM eclipse-temurin:17-jre
COPY --from=build /app/target/*.jar  app.jar
ENTRYPOINT ["java","--add-exports","java.base/sun.nio.ch=ALL-UNNAMED","-jar","app.jar"]